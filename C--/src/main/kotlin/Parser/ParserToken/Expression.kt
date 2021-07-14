package Parser.ParserToken

sealed class Expression : ILineOfCode
{
    // Used for parser internaly
    public var BlockDepth = -1

    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    // Operator is a value
    data class Operation(val operator: Operator, val expressionA: Expression, val expressionB: Expression?, override val LineOfCode: Int = -1) : Expression()

    data class UseVariable(val variableName : String, override val LineOfCode: Int = -1) : Expression()

    data class FunctionCall(val functionName : String, val parameterList : List<Expression>? , override val LineOfCode: Int = -1) : Expression()


    data class Value(val value: ConstantValue, override val LineOfCode: Int = -1) : Expression()
}