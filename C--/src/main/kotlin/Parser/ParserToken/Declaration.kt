package Parser.ParserToken

sealed class Declaration // Lila
{
    override fun toString(): String{
        return this.javaClass.simpleName
    }

    data class FunctionDeclare(
        val returnType: Type,
        val functionName: String,
        val body: Body,
        val parameters : List<Parameter>?
    ) : Declaration()

    data class VariableDeclaration(val type: Type, val name: String, val expression : Expression) : Declaration()
}