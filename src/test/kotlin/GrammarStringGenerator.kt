import io.kotest.property.arbitrary.next

// used kotest to generate expressions for me, sometimes it stack overflows
// if i use too many binaryExpressions though and it's kinda hard to verify the values using tests
// could be useful in the future though
fun main() {
    val gen1 = expressionGenerator()
    val parser = RecursiveParser()

    var input = gen1.next()
    println("input is $input")
    println("Recursive Parser is ${parser.evaluateInput(input)}")
    println(parser.parseFromString(input))

    for (i in 0..10) {
        input = gen1.next()
        println("input: $i is $input")
        println("Recursive Parser: $i is ${parser.evaluateInput(input)}")
        println(parser.parseFromString(input))
    }
}