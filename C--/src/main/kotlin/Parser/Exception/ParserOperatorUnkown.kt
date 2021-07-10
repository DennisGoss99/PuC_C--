package Parser.Exception

import Lexer.LexerToken

class ParserOperatorUnkown(val token : LexerToken) : Exception("Operator unkown. <$token>")
{
}