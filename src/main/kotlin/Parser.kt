const val STR_ELEMENT = "element"
// by default set this to 0
const val ELEMENT_VALUE: Long = 0

class ArithmeticParser(input: String) {
    private val tokens: List<Token> = lexer(input)

    fun evaluateExpression(): Long {
        return evaluateRPN(shuntingYard())
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

            }
        }

        return stack.removeLast()
    }


    // converts tokens into RPN
    private fun shuntingYard(): ArrayDeque<Token> {
        val outputQueue = ArrayDeque<Token>()
        val operatorStack = ArrayDeque<Token>()

        for (token in tokens) {
            when {
                token.type == TokenType.NUMBER -> outputQueue.add(token)
                token.type == TokenType.ELEMENT -> outputQueue.add(token)
                token.type == TokenType.LEFT_PAREN  -> operatorStack.add(token)
                token.type == TokenType.RIGHT_PAREN -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last().type != TokenType.LEFT_PAREN) {
                        outputQueue.add(operatorStack.removeLast())
                    }
                    operatorStack.removeLast()
                }
                token.isOperator() -> operatorStack.add(token)
            }
        }

        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.removeLast())
        }

        return outputQueue
    }

    private fun applyOperator(operator: TokenType, operand1: Long, operand2: Long): Long {
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
            else -> false
        }
    }
}

fun lexer(input: String): List<Token> {
    val tokens = mutableListOf<Token>()
    var i = 0

    while (i < input.length) {
        when (val char = input[i]) {
            'e' -> {
                require(input.substring(i, i + STR_ELEMENT.length) == STR_ELEMENT)
                tokens.add(Token(TokenType.ELEMENT, STR_ELEMENT))
                i += STR_ELEMENT.length
            }
            in '0'..'9' -> {
                var number = char.toString()
                i++
                while (i < input.length && input[i] in '0'..'9') {
                    number += input[i]
                    i++
                }
                tokens.add(Token(TokenType.NUMBER, number))
            }
            '+' -> {
                tokens.add(Token(TokenType.PLUS, "+"))
                i++
            }
            '-' -> {
                tokens.add(Token(TokenType.MINUS, "-"))
                i++
            }
            '*' -> {
                tokens.add(Token(TokenType.MULTIPLY, "*"))
                i++
            }
            '(' -> {
                tokens.add(Token(TokenType.LEFT_PAREN, "("))
                i++
            }
            ')' -> {
                tokens.add(Token(TokenType.RIGHT_PAREN, ")"))
                i++
            }
            else -> {
                // increments if its whitespace otherwise throws exception
                require(char.isWhitespace())
                i++
            }
        }
    }

    return tokens
}