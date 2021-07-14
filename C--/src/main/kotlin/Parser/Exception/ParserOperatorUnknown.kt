package Parser.Exception

import Lexer.LexerToken

class ParserOperatorUnknown(val token : LexerToken) : ParserBaseException(token.LineOfCode, "Operator unkown. <$token>")