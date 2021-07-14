package Parser.Exception

import Lexer.LexerToken

class ParserTokenUnexpected(val token : LexerToken) : ParserBaseException(token.LineOfCode, "Token <$token> was not expected")
{
}