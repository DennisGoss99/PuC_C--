package Parser.Exception

import Lexer.LexerToken

class ParserTypeUnknown(val invalidToken : LexerToken) : ParserBaseException(invalidToken.LineOfCode, "Unkown type <$invalidToken>")
{
}