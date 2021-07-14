package Lexer.Exceptions

class LexerUnexpectedCharException : LexerBaseException{
    val unexpectedChar : Char

    constructor(lineOfCode: Int, unexpectedChar : Char) : super(lineOfCode, "Unexpected char: '$unexpectedChar'"){
        this.unexpectedChar = unexpectedChar
    }

    constructor(lineOfCode: Int, unexpectedChar : Char, beforeChar: Char) : super(lineOfCode, "Unexpected char: '$unexpectedChar' after char '$beforeChar'"){
        this.unexpectedChar = unexpectedChar
    }


}