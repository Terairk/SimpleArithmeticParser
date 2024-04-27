import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

// make sure Lexer produces the correct List of Tokens for next step
class LexerTest : FunSpec({
    test("lexer with element") {
        testHelper("element", listOf(Token(TokenType.ELEMENT, "element")))
    }

    test("lexer with number") {
        testHelper("123", listOf(Token(TokenType.NUMBER, "123")))
    }

    test("lexer with unary minus") {
        testHelper(
            "-42",
            listOf(
                Token(TokenType.MINUS, "-"),
                Token(TokenType.NUMBER, "42"),
            ),
        )
    }

    test("lexer with binary expression") {
        testHelper(
            "(element + 10)",
            listOf(
                Token(TokenType.LEFT_PAREN, "("),
                Token(TokenType.ELEMENT, "element"),
                Token(TokenType.PLUS, "+"),
                Token(TokenType.NUMBER, "10"),
                Token(TokenType.RIGHT_PAREN, ")"),
            ),
        )
    }

    test("lexer but with different whitespaces") {
        lexer("(element + 10) * (-533)") shouldBe lexer("(element+10)*(-533)")
    }

    test("lexer should fail on incorrectly parsed element") {
        shouldThrow<IllegalArgumentException> {
            lexer("ele ment")
        }
    }
})

private fun testHelper(
    input: String,
    expectedTokens: List<Token>,
) {
    lexer(input) shouldBe expectedTokens
}
