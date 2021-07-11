package TypeChecker.Exceptions

import Parser.ParserToken.Type

class TypeCheckerReturnTypeException(expectedType: Type?, actualType : Type ) : Exception("Return type '$actualType' doesn't match Type '$expectedType'")