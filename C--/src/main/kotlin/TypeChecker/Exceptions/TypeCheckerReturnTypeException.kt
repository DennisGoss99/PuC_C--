package TypeChecker.Exceptions

import Parser.ParserToken.Type

class TypeCheckerReturnTypeException(lineOfCode : Int,functionName : String, expectedType: Type?, actualType : Type ) : TypeCheckerBaseException(lineOfCode, "Function return type '$expectedType' of function: '$functionName' doesn't match Type '$actualType'")