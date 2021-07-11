package TypeChecker.Exceptions

import Parser.ParserToken.*

class TypeCheckerWrongTypeAssignmentException(variableName: String,expectedType: Type?, actualType : Type)
    : Exception("Cant assign type '$actualType' to variable '$variableName of Type '$expectedType''")