package Parser.Exception

import Lexer.LexerToken

class ParserValueUnkown(val invalidToken : LexerToken) : Exception("Unkown value (literal). Token:<$invalidToken>")
{
}