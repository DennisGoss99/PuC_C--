package Parser.ParserToken

sealed class Statement : ILineOfCode // Grün
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    data class AssignValue(val variableName : String, val expression: Expression, override val LineOfCode: Int = -1) : Statement()

    data class ProcedureCall(val procedureName : String, val parameterList : List<Expression>? , override val LineOfCode: Int = -1) : Statement()

    data class If(val condition : Expression, val ifBody : Body, val elseBody : Body?, override val LineOfCode: Int = -1) : Statement()
    data class While(val condition : Expression, val body : Body, override val LineOfCode: Int = -1) : Statement()
    data class Block(val body : Body, override val LineOfCode: Int = -1): Statement()
}