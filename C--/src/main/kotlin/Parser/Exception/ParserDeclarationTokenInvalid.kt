package Parser.Exception

import Lexer.LexerToken

class ParserDeclarationTokenInvalid(val invalidToken : LexerToken) : ParserBaseException(invalidToken.LineOfCode, "Invalid declaration. Token:<$invalidToken>")
{
}