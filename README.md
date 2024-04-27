# Keymap Translator

## Project Description
This is my submission for the "**Keymap translator**" task for Jetbrains Internship. 

# Tools used
- **Kotest**: Popular testing framework for Kotlin that supports property testing and DSL's for writing tests.
  - Used for having a custom generator that allows me to generate arbritrary strings that may or may not conform to the grammar for testing purposes.
- **Detekt**: Popular library for static code analysis and code smell.

# Setup
You may need to install the Kotest plugin if you're using IntelliJ IDEA to run the tests from File/IntelliJ (there should be a view for it in IntelliJ)
Alternatively, you can right click the test folder and hit run tests.
You should also be able to do ./gradlew clean test to see verbose tests.

You may need to reload gradle changes in build.gradle.kts (but should be done automatically). I didn't make a main application however the tests should be able to showcase the features off.
