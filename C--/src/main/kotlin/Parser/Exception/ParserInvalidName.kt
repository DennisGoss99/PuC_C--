package Parser.Exception

import Lexer.LexerToken

class ParserInvalidName(val token : LexerToken) : ParserBaseException(token.LineOfCode,"Invalid name. Expected name but got <$token>")
{

}