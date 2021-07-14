package TypeChecker.Exceptions

import Parser.ParserToken.*

class TypeCheckerWrongTypeAssignmentException(lineOfCode : Int,variableName: String,expectedType: Type?, actualType : Type)
    : TypeCheckerBaseException(lineOfCode, "Cant assign type '$actualType' to variable '$variableName of Type '$expectedType''")