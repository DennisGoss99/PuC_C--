package Lexer

sealed class LexerToken() {

        public override fun toString(): String {
            return this.javaClass.simpleName
        }

        open val LineOfCode : Int = -1

        // Keywords
        data class If(override val LineOfCode : Int = -1) : LexerToken()
        data class Else(override val LineOfCode : Int = -1) : LexerToken()
        data class While(override val LineOfCode : Int = -1) : LexerToken()
        data class Return(override val LineOfCode : Int = -1) : LexerToken()
        data class Struct(override val LineOfCode : Int = -1) : LexerToken()

        // Symbols
        data class AssignEquals(override val LineOfCode : Int = -1) : LexerToken()       // =
        data class Equals(override val LineOfCode : Int = -1) : LexerToken()             // :=
        data class Semicolon(override val LineOfCode : Int = -1) : LexerToken()          // ;
        data class Lparen(override val LineOfCode : Int = -1) : LexerToken()             // (
        data class Rparen(override val LineOfCode : Int = -1) : LexerToken()             // )
        data class LBracket(override val LineOfCode : Int = -1) : LexerToken()           // [
        data class RBracket(override val LineOfCode : Int = -1) : LexerToken()           // ]
        data class LCurlyBrace(override val LineOfCode : Int = -1) : LexerToken()        // {
        data class RCurlyBrace(override val LineOfCode : Int = -1) : LexerToken()        // }
        data class Comma(override val LineOfCode : Int = -1) : LexerToken()              // ,

        // Operatoren
        data class Plus(override val LineOfCode : Int = -1) : LexerToken()               // +
        data class Minus(override val LineOfCode : Int = -1) : LexerToken()              // -
        data class Mul(override val LineOfCode : Int = -1) : LexerToken()                // *
        data class Double_Equals(override val LineOfCode : Int = -1) : LexerToken()      // ==

        data class And(override val LineOfCode : Int = -1) : LexerToken()                // &&
        data class Or(override val LineOfCode : Int = -1) : LexerToken()                 // ||
        data class Not(override val LineOfCode : Int = -1) : LexerToken()                // !
        data class NotEqual(override val LineOfCode : Int = -1) : LexerToken()           // !=
        data class Less(override val LineOfCode : Int = -1) : LexerToken()               // <
        data class LessEqual(override val LineOfCode : Int = -1) : LexerToken()          // <=
        data class Greater(override val LineOfCode : Int = -1) : LexerToken()            // >
        data class GreaterEqual(override val LineOfCode : Int = -1) : LexerToken()       // >=

        data class NameIdent(val identify: String, override val LineOfCode : Int = -1) : LexerToken()
        data class TypeIdent(val identify: String, override val LineOfCode : Int = -1) : LexerToken()
        data class FunctionIdent(val identify: String, override val LineOfCode : Int = -1) : LexerToken()

        // Literals
        data class Boolean_Literal(val b: Boolean, override val LineOfCode : Int = -1) : LexerToken()
        data class Number_Literal(val n: Int, override val LineOfCode : Int = -1) : LexerToken()
        data class Char_Literal(val c: Char, override val LineOfCode : Int = -1) : LexerToken()
        data class String_Literal(val s: String, override val LineOfCode : Int = -1) : LexerToken()

        // Control Token
        object EOF : LexerToken()
}