package TypeChecker.Exceptions

import Parser.ParserToken.Operator

class TypeCheckerOperationException(lineOfCode : Int,message : String, operator : Operator) : TypeCheckerBaseException(lineOfCode, "$message Operator:'$operator'")