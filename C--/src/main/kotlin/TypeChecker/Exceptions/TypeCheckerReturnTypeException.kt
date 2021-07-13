package TypeChecker.Exceptions

import Parser.ParserToken.Type

class TypeCheckerReturnTypeException(functionName : String, expectedType: Type?, actualType : Type ) : Exception("Function return type '$expectedType' of function: '$functionName' doesn't match Type '$actualType'")