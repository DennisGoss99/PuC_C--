package Parser.ParserToken

sealed class ConstantValue
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    data class ConstInteger(val value : Int, val type: Type.Integer = Type.Integer) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }
    }
}