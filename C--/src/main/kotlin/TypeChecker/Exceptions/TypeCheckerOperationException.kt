package TypeChecker.Exceptions

import Parser.ParserToken.Operator

class TypeCheckerOperationException(message : String, operator : Operator) : Exception("$message Operator:'$operator'")