package tool.primarytype

fun Char.matchingBracket() : Char =
    when(this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'

        ')' -> '('
        ']' -> '['
        '}' -> '{'
        '>' -> '<'
        else -> this
    }

fun Char.isOpeningBracket() = this in "([{<"
fun Char.isClosingBracket() = this in ")]}>"
