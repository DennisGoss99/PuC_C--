package TypeChecker.Exceptions

open class TypeCheckerBaseException(lineOfCode: Int, message: String) : Exception("[Error at line:$lineOfCode] $message")