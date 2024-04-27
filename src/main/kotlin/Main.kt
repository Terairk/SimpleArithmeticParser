fun main() {
    val input = "(((4 * 7) + 2) - 8)"
    println("Result: ${ArithmeticParser(input).evaluateExpression()}")
}