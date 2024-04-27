// This class is what I'd do if I were to use a RecursiveDescentParser
// This would allow me greater control compared to shunting yard and allow
// for better error handling
class RecursiveParser : ArithmeticParser {
    private var tokens: List<Token> = emptyList()
    private var currentIndex = 0

    override fun evaluateInput(input: String): Long {
        val expression = parseFromString(input)
        return evaluateExpression(expression)
    }

    fun parseFromString(input: String): Expression {
        // reset things to an "empty" state
        tokens = lexer(input)
        currentIndex = 0
        val result = parseExpression()
        require(currentIndex == tokens.size) { "Unexpected token: ${tokens[currentIndex]}"}
        return result
    }

    private fun evaluateExpression(expression: Expression): Long {
        return when (expression) {
            is ElementExpression -> {
                // This default's to 0 as defined in ConstantsKt
                ELEMENT_VALUE
            }
            is ConstantExpression -> {
                return expression.value
            }
            is BinaryExpression -> {
                val leftValue = evaluateExpression(expression.left)
                val rightValue = evaluateExpression(expression.right)
                when (expression.operation) {
                    TokenType.PLUS -> leftValue + rightValue
                    TokenType.MINUS -> leftValue - rightValue
                    TokenType.MULTIPLY -> leftValue * rightValue
                    else -> throw IllegalArgumentException("Unsupported Operation: ${expression.operation}")
                }
            }
        }
    }

    private fun parseExpression(): Expression {
        return when (peek()?.type) {
            TokenType.ELEMENT -> parseElement()
            TokenType.MINUS, TokenType.NUMBER -> parseConstantExpression()
            TokenType.LEFT_PAREN -> parseBinaryExpression()
            else -> throw IllegalArgumentException("Invalid expression")
        }
    }

    private fun parseConstantExpression(): Expression {
        return when (peek()?.type) {
            TokenType.MINUS -> {
                consume(TokenType.MINUS)
                val number = consume(TokenType.NUMBER).value.toLong()
                ConstantExpression(-number)
            }
            TokenType.NUMBER -> {
                val number = consume(TokenType.NUMBER).value.toLong()
                ConstantExpression(number)
            }
            else -> throw IllegalArgumentException("Invalid constant expression")
        }
    }

    private fun parseElement(): Expression {
        consume(TokenType.ELEMENT)
        return ElementExpression
    }

    private fun parseBinaryExpression(): Expression {
        consume(TokenType.LEFT_PAREN)
        val left = parseExpression()
        val operationToken = consume(TokenType.PLUS, TokenType.MINUS, TokenType.MULTIPLY)
        val right = parseExpression()
        consume(TokenType.RIGHT_PAREN)
        return BinaryExpression(left, operationToken.type, right)
    }

    private fun peek(): Token? {
        if (currentIndex < tokens.size) {
            return tokens[currentIndex]
        }
        return null
    }

    private fun consume(vararg expectedTypes: TokenType): Token {
        val currentToken = tokens[currentIndex]
        require(currentToken.type in expectedTypes) { "Unexpected token: $currentToken at ${currentToken.location}"}
        currentIndex++
        return currentToken
    }
}

// models the classes needed for this recursive parser

sealed interface Expression
object ElementExpression : Expression
data class ConstantExpression(val value: Long) : Expression
data class BinaryExpression(val left: Expression, val operation: TokenType, val right: Expression): Expression
