package TypeChecker.Exceptions

class TypeCheckerFunctionNotFoundException(lineOfCode : Int,functionName : String) : TypeCheckerBaseException(lineOfCode, "Couldn't find function: '$functionName'")