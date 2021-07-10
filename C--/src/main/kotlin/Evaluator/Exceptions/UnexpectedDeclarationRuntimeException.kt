package Evaluator.Exceptions

import Parser.ParserToken.Declaration

class UnexpectedDeclarationRuntimeException(val declaration: Declaration) : Exception("Found unexpected declaration: '$declaration'")