package Parser.ParserToken

class Parameter(val name : String, val type : Type)
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }
}
