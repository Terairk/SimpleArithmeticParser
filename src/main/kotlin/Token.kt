data class Token(val type: TokenType, val value: String, val location: Int = -1) {
    // had to override equals for data class so that tokens of different location
    // but of the same type can be considered equal. Need location for error handling.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Token) return false

        if (type != other.type) return false
        if (value != other.value) return false

        return true
    }

    // generic hashCode
    override fun hashCode(): Int {
        return 31 * type.hashCode() + value.hashCode()
    }
}

// performs lexical analysis and tokenizes the input
fun lexer(input: String): List<Token> {
    val tokens = mutableListOf<Token>()
    var i = 0

    // do some basic error handling here as it's harder to do it in the shunting yard place
    // can do better error handling in the recursive descent parser
    // we can't really match number of parenthesis with number of operators due to
    // the unary minus
    require(input.count { it == '(' } == input.count { it == ')' })

    while (i < input.length) {
        when (val char = input[i]) {
            'e' -> {
                require(input.substring(i, i + STR_ELEMENT.length) == STR_ELEMENT)
                tokens.add(Token(TokenType.ELEMENT, STR_ELEMENT, i))
                i += STR_ELEMENT.length
            }
            in '0'..'9' -> {
                val initialIndex = i
                val number =
                    buildString {
                        append(char)
                        i++
                        while (i < input.length && input[i] in '0'..'9') {
                            append(input[i])
                            i++
                        }
                    }
                tokens.add(Token(TokenType.NUMBER, number, initialIndex))
            }
            '+' -> {
                tokens.add(Token(TokenType.PLUS, "+", i))
                i++
            }
            '-' -> {
                tokens.add(Token(TokenType.MINUS, "-", i))
                i++
            }
            '*' -> {
                tokens.add(Token(TokenType.MULTIPLY, "*", i))
                i++
            }
            '(' -> {
                tokens.add(Token(TokenType.LEFT_PAREN, "(", i))
                i++
            }
            ')' -> {
                tokens.add(Token(TokenType.RIGHT_PAREN, ")", i))
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
