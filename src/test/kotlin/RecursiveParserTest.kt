import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RecursiveParserTest : FunSpec({
    test("Valid expressions") {
        with(RecursiveParser()) {
            evaluateInput("(1 + 2)") shouldBe 3
            evaluateInput("(4 * 5)") shouldBe 20
            evaluateInput("(10 - 3)") shouldBe 7
            evaluateInput("(2 + (3 * 4))") shouldBe 14
            evaluateInput("((1 + 2) * 3)") shouldBe 9
            evaluateInput("(7 - (3 + 2))") shouldBe 2
        }
    }

    test("Invalid expressions") {
        with(RecursiveParser()) {
            shouldThrow<IllegalArgumentException> {
                evaluateInput("1 2 +")
            }
            shouldThrow<IllegalArgumentException> {
                evaluateInput("(1 + 2")
            }
            shouldThrow<IllegalArgumentException> {
                evaluateInput("1 + 2)")
            }
            shouldThrow<IllegalArgumentException> {
                evaluateInput("1 + * 2")
            }
            shouldThrow<IllegalArgumentException> {
                evaluateInput("1 + 2")
            }
        }
    }

    test("Single number") {
        with(RecursiveParser()) {
            evaluateInput("42") shouldBe 42
        }
    }

    test("Negative numbers") {
        with(RecursiveParser()) {
            evaluateInput("-5") shouldBe -5
            evaluateInput("(2 + -3)") shouldBe -1
            evaluateInput("(-2 * 4)") shouldBe -8
        }
    }

    test("Whitespace handling") {
        with(RecursiveParser()) {
            evaluateInput("( 1+2)") shouldBe 3
            evaluateInput("(1 + 2 )") shouldBe 3
            evaluateInput("(  1  +  2  )") shouldBe 3
        }
    }

    test("element handling") {
        with(RecursiveParser()) {
            evaluateInput("element") shouldBe ELEMENT_VALUE
        }
    }

    test("complex expression") {
        with(RecursiveParser()) {
            evaluateInput("(((5 * 8) * -7) + (-8 + 6))") shouldBe -282
            evaluateInput(
                "(-170 - (293 - ((((-199 - -150) * (269 + -90)) " +
                    "- ((19 * (-289 + 63)) - (49 + 68))) * (63 + 62))))",
            ) shouldBe -545463
        }
    }
})
