class ShuntingYardParser : ArithmeticParser {
    override fun evaluateInput(input: String): Long {
        return evaluateRPN(shuntingYard(input))
    }

    private fun evaluateRPN(rpnTokens: ArrayDeque<Token>): Long {
        val stack = ArrayDeque<Long>()

        for (token in rpnTokens) {
            when {
                token.type == TokenType.NUMBER -> stack.add(token.value.toLong())
                token.type == TokenType.ELEMENT -> stack.add(ELEMENT_VALUE)
                token.isOperator() -> {
                    val operand2 = stack.removeLast()
                    val operand1 = stack.removeLast()
                    val result = applyOperator(token.type, operand1, operand2)
                    stack.add(result)
                }
                else -> {
                    throw IllegalArgumentException("unknown token")
                }
            }
        }

        return stack.removeLast()
    }

    // converts tokens into RPN
    private fun shuntingYard(input: String): ArrayDeque<Token> {
        val tokens: List<Token> = lexer(input)
        val outputQueue = ArrayDeque<Token>()
        val operatorStack = ArrayDeque<Token>()

        var previousToken: Token? = null
        var index = 0
        while (index < tokens.size) {
            val token = tokens[index]
            when {
                token.type == TokenType.NUMBER -> outputQueue.add(token)
                token.type == TokenType.ELEMENT -> outputQueue.add(token)
                token.type == TokenType.LEFT_PAREN -> operatorStack.add(token)
                token.type == TokenType.RIGHT_PAREN -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last().type != TokenType.LEFT_PAREN) {
                        val pot = operatorStack.removeLast()
                        outputQueue.add(pot)
                    }
                    // removes the left parenthesis
                    operatorStack.removeLast()
                }
                token.isOperator() -> {
                    val canBeUnaryMinus = previousToken == null ||
                            previousToken.isOperator() ||
                            previousToken.type == TokenType.LEFT_PAREN
                    if (canBeUnaryMinus && token.type == TokenType.MINUS) {
                        index++
                        // guaranteed to be a number
                        val newNumber = "${-tokens[index].value.toLong()}"
                        outputQueue.add(Token(TokenType.NUMBER, newNumber))
                    } else {
                        operatorStack.add(token)
                    }
                }
            }

            previousToken = tokens[index]
            index++
        }

        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.removeLast())
        }

        return outputQueue
    }

    private fun applyOperator(
        operator: TokenType,
        operand1: Long,
        operand2: Long,
    ): Long {
        return when (operator) {
            TokenType.PLUS -> operand1 + operand2
            TokenType.MINUS -> operand1 - operand2
            TokenType.MULTIPLY -> operand1 * operand2
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
    }

    private fun Token.isOperator(): Boolean {
        return when (this.type) {
            TokenType.PLUS, TokenType.MULTIPLY, TokenType.MINUS -> true
            else                                                -> false
        }
    }
}
