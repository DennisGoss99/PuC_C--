package Parser.Exception

import Lexer.LexerToken

class ParserInvalidDeclarationToken(val invalidToken : LexerToken) : Exception("Invalid declaration. Token:<$invalidToken>")
{
}