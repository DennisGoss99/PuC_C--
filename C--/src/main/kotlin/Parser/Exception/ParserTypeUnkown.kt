package Parser.Exception

import Lexer.LexerToken

class ParserTypeUnkown(val invalidToken : LexerToken) : Exception("Unkown type <$invalidToken>")
{
}