package Parser.ParserToken

sealed class Type
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    object Boolean : Type()
    object Char: Type()
    object Integer: Type()
    object Float : Type()
    object Double : Type()
    object String : Type()
    object Void : Type()
}