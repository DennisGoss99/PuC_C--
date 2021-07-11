package TypeChecker.Exceptions

class TypeCheckerVariableNotFoundException (variableName : String) : Exception("Couldn't find variable: '$variableName'")