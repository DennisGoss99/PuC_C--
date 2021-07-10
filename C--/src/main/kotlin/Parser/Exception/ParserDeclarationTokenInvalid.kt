package Parser.Exception

import Lexer.LexerToken

class ParserDeclarationTokenInvalid(val invalidToken : LexerToken) : Exception("Invalid declaration. Token:<$invalidToken>")
{
}