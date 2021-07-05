package Parser.ParserToken

class Body(val functionBody : List<Statement>, val localVariables : List<Declaration.VariableDeclaration>? = null)
{
    override fun toString(): String
    {
        return "{${functionBody.toString()}},LocalVariables{${localVariables?.toString()}} "
    }
}