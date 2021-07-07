package Parser.ParserToken

sealed class Statement // Grün
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    data class AssignValue(val value : Type, val expression: Expression) : Statement()



    data class If(val condition : Expression, val ifBody : Body, val elseBody : Body?) : Statement()
    data class While(val condition : Expression, val body : Body) : Statement()
    data class Block(val body : Body): Statement()
}