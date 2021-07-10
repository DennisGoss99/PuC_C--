package Evaluator.Exceptions

import Parser.ParserToken.Type

class TypeMismatchRuntimeException(message: String, expectedType : Type) : Exception("$message '$expectedType'")