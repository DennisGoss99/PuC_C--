package Parser.Exception

import Lexer.LexerToken

class ParserStatementInvalid(val token : LexerToken) : ParserBaseException(token.LineOfCode, "Invalid statement. Expected ',' but got <$token>")
{
}