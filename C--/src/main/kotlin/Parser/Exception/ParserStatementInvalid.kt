package Parser.Exception

import Lexer.LexerToken

class ParserStatementInvalid(val token : LexerToken) : Exception("Invalid statement. Expected ',' but got <$token>")
{
}