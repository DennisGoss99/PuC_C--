package TypeChecker.Exceptions

class TypeCheckerVariableNotFoundException (lineOfCode : Int,variableName : String) : TypeCheckerBaseException(lineOfCode, "Couldn't find variable: '$variableName'")