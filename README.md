# Keymap Translator

## Project Description
This is my submission for the "**Keymap translator**" task for Jetbrains Internship.
There's an example in MainKt and also GeneratorMainKt under test/kotlin if you want to use the generator
(The generator uses Kotest's property testing methods such as Arbritrary)
You may need to run GeneratorMain's main function a few times as it could theoretically stackoverflow when using the generators
as it might StackOverflow when generating a really long string.

# Tools used
- **Kotest**: Popular testing framework for Kotlin that supports property testing and DSL's for writing tests.
  - Used for having a custom generator that allows me to generate arbritrary strings that may or may not conform to the grammar for testing purposes.
  - The generator ended up being helpful to generate well-formed long queries rather than doing it manually (and I also got to get more experience with Kotest)
- **Detekt**: Popular library for static code analysis and code smell. Used this along with Ktlint to ensure my code didn't 'smell'.

# Setup
You may need to install the Kotest plugin if you're using IntelliJ IDEA to run the tests from File/IntelliJ (there should be a view for it in IntelliJ)
Alternatively, you can right click the test folder and hit run tests.
You should also be able to do ./gradlew clean test to see verbose tests.

You may need to reload gradle changes in build.gradle.kts (but should be done automatically).

# Commentary
## Shunting Yard Algorithm
I initially saw that the grammar was pretty well formed (and also no "obvious" operator precedence) and since I learnt about shunting yard algorithm,
my first attempt was to use the shunting yard algorithm. The hidden unary minus was hard as my code assumed no operator precedence.
It took a long time to get the unary minus in <constant-expression> done right using this but
I managed it in the end. The upside is that ShuntingYard won't suffer from Stack overflows and will be O(n) in running time and won't take memory overhead.

## Recursive Descent Parser
My 2nd attempt was using a somewhat recursive descent parser (this was my initial idea but I thought shunting yard would be easier at first). 
After actually coding up the recursive parser, it was much easier to actually code it up than I expected. I've also coded up a recursive descent parser 
before for an interpreter. The nature of evaluating the "AST" was quite straightforward and felt quite familliar having done many haskell tests which
were quite similar in retrospect.

Overall I had quite a lot of fun with this task.
