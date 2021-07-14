package Parser.Exception

open class ParserBaseException(lineOfCode: Int, message: String) : Exception("[Error at line:$lineOfCode] $message")