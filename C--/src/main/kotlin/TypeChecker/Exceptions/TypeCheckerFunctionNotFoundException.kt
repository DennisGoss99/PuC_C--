package TypeChecker.Exceptions

class TypeCheckerFunctionNotFoundException(functionName : String) : Exception("Couldn't find function: '$functionName'")