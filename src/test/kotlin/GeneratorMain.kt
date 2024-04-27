import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choose


// used kotest to generate expressions for me, sometimes it stack overflows
// if I use too many binaryExpressions though, and it's kinda hard to verify the values using tests
// could be useful in the future though
fun main() {
    val gen1 = expressionGenerator
    val parser = RecursiveParser()

    var input: String

    for (i in 0..10) {
        input = gen1.next()
        println("input #$i: input is $input")
        println("Recursive Parser #$i: value is ${parser.evaluateInput(input)}")
        println(parser.parseFromString(input))
        println("=".repeat(32))
    }
}

val numberGenerator: Arb<String> = Arb.long(0,300).map { it.toString() }

val signGenerator = Arb.element("", "-")
val operationGenerator = Arb.element("+", "-", "*")
val elementGenerator = Arb.stringPattern("element")

val constantExpressionGenerator: Arb<String> =
    arbitrary {
        val sign = signGenerator.bind()
        val number = numberGenerator.bind()
        "$sign$number"
    }

val binaryExpressionGenerator: Arb<String> =
    arbitrary {
        val left = expressionGenerator.bind()
        val operation = operationGenerator.bind()
        val right = expressionGenerator.bind()
        "($left $operation $right)"
    }

val expressionGenerator: Arb<String> =
        Arb.choose(
            2 to elementGenerator,
            40 to constantExpressionGenerator,
            30 to binaryExpressionGenerator,
        )
