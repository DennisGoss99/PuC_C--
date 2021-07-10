package Parser.Exception

import Lexer.LexerToken

class ParserTokenUnexpected(val token : LexerToken) : Exception("Token <$token> was not expected")
{
}