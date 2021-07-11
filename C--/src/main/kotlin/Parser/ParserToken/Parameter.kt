package Parser.ParserToken

class Parameter(val name : String, val type : Type)
{
    override fun toString(): kotlin.String
    {
        return "Parameter{name=$name, type=$type}"
    }
}
