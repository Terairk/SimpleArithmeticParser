import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.property.checkAll

// this test doesn't do much but you can see that it produces valid strings
// in GrammarStringGeneratorKt.main()
class GrammarStringGeneratorTest : FunSpec({
    test("generated strings should conform to the grammar") {
        checkAll(expressionGenerator) { generatedString ->
            generatedString.length shouldBeGreaterThan (0)
        }
    }
})
