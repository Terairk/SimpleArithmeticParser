import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.choose
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll

// this test doesn't do much but you can see that it produces valid strings
// in GrammarStringGeneratorKt.main()
class GrammarStringGeneratorTest : FunSpec({
    test("generated strings should conform to the grammar") {
        checkAll(expressionGenerator()) { generatedString ->
            generatedString.length shouldBeGreaterThan(0)
        }
    }
})


fun numberGenerator(): Arb<String> = arbitrary {
    val number = Arb.long(0, 300)
    number.next().toString()
}

fun operationGenerator(): Arb<String> = arbitrary { Arb.element("+", "-", "*").next() }

fun constantExpressionGenerator(): Arb<String> = arbitrary {
    val sign = arbitrary { Arb.element("", "-").next() }.next()
    val number = numberGenerator().next()
    sign + number
}

fun binaryExpressionGenerator(): Arb<String> = arbitrary {
    val left = expressionGenerator().next()
    val operation = operationGenerator().next()
    val right = expressionGenerator().next()
    "($left $operation $right)"
}

fun expressionGenerator(): Arb<String> = arbitrary {
    Arb.choose(
        2 to Arb.stringPattern("element"),
        40 to constantExpressionGenerator(),
        30 to binaryExpressionGenerator()
    ).next()
}

