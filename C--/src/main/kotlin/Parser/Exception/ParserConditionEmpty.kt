package Parser.Exception

class ParserConditionEmpty(lineOfCode: Int) : ParserBaseException(lineOfCode, "Condition can't be empty. Specify an expression has yield a boolean.")