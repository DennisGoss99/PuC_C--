package Parser.Exception

import Lexer.LexerToken

class ParserExpressionInvalid(val token : LexerToken) : Exception("Expected expression but got <$token>")
{

}