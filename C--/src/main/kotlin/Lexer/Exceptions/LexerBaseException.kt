package Lexer.Exceptions

open class LexerBaseException(lineOfCode: Int, message: String) : Exception("[Error at line:$lineOfCode] $message")