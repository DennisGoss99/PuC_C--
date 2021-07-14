package Parser.Exception

import Lexer.LexerToken

class ParserValueUnknown(val invalidToken : LexerToken) : ParserBaseException(invalidToken.LineOfCode, "Unkown value (literal). Token:<$invalidToken>")
{
}