fun main() {
    val parser = RecursiveParser()
    val input =
        "(-170 - (293 - ((((-199 - -150) * (269 + -90)) " +
            "- ((19 * (-289 + 63)) - (49 + 68))) * (63 + 62))))"

    println("Input is $input")
    println("Evaluating input: ${parser.evaluateInput(input)}")
    // more examples are in GrammarMainKt
}
