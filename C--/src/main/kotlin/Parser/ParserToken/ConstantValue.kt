package Parser.ParserToken

sealed class ConstantValue
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    data class ConstBoolean(val value : Boolean, val type: Type.Boolean = Type.Boolean) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }

    }

    data class ConstChar(val value : Char, val type: Type.Char = Type.Char) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }
    }

    data class ConstInteger(val value : Int, val type: Type.Integer = Type.Integer) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }
    }

    data class ConstFloat(val value : Float, val type: Type.Float = Type.Float) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }
    }
}