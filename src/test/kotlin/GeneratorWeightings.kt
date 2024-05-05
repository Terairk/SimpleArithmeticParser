// lower the BINARY_GEN_WEIGHT if having problems with StackOverflow
// Since the binary expressions are what makes it arbitrarily long.

const val ELEMENT_GEN_WEIGHT = 2
const val CONSTANT_GEN_WEIGHT = 40
const val BINARY_GEN_WEIGHT = 30
