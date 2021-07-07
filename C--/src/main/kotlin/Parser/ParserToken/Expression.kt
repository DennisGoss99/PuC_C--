package Parser.ParserToken

sealed class Expression
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    // Operator is a value
    data class Operation(val operator: Operator, val expressionA: Expression, val expressionB: Expression?) : Expression()

    data class UseVariable(val variableName : String) : Expression()

    data class FunctionCall(val functionName : String, val parameterList : List<Expression>? ) : Expression()


    data class Value(val value: ConstantValue) : Expression()
}