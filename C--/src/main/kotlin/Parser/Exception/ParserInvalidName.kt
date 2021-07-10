package Parser.Exception

import Lexer.LexerToken

class ParserInvalidName(val token : LexerToken) : Exception("Invalid name. Expected name but got <$token>")
{

}