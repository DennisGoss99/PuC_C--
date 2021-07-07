package Parser.ParserToken

sealed class ConstantValue
{
    override fun toString(): kotlin.String
    {
        return this.javaClass.simpleName
    }

    data class Boolean(val value : kotlin.Boolean, val type: Type.Boolean = Type.Boolean) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }

    }

    data class Char(val value : kotlin.Char, val type: Type.Char = Type.Char) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }
    }

    data class Integer(val value : Int, val type: Type.Integer = Type.Integer) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }
    }

    data class Float(val value : kotlin.Float, val type: Type.Float = Type.Float) : ConstantValue()
    {
        override fun toString(): kotlin.String
        {
            return "$value : $type"
        }
    }
}