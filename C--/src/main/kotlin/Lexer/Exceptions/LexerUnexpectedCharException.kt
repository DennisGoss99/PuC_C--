package Lexer.Exceptions

class LexerUnexpectedCharException: Exception{
    val unexpectedChar : Char

    constructor(unexpectedChar : Char) : super("Unexpected char: '$unexpectedChar'"){
        this.unexpectedChar = unexpectedChar
    }

    constructor(unexpectedChar : Char, beforeChar: Char) : super("Unexpected char: '$unexpectedChar' after char '$beforeChar'"){
        this.unexpectedChar = unexpectedChar
    }


}