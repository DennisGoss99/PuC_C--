package Parser.Exception

import Lexer.LexerToken

class ParserExpressionInvalid(val token : LexerToken) : ParserBaseException(token.LineOfCode, "Expected expression but got <$token>")
{

}