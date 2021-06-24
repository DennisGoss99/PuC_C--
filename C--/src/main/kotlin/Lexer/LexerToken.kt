package Lexer

sealed class LexerToken {

        override fun toString(): String {
            return this.javaClass.simpleName
        }

        // Keywords
        object If : LexerToken()
        object Else : LexerToken()
        object While : LexerToken()
        object Return : LexerToken()
        object Struct : LexerToken()
        object Void : LexerToken()


        // Symbols

        object Equals : LexerToken()             // =
        object Semicolon : LexerToken()          // ;
        object Lparen : LexerToken()             // (
        object Rparen : LexerToken()             // )
        object LBracket : LexerToken()           // [
        object RBracket : LexerToken()           // ]
        object LCurlyBrace : LexerToken()        // {
        object RCurlyBrace : LexerToken()        // }
        object Comma : LexerToken()              // ,

        // Operatoren
        object Plus : LexerToken()               // +
        object Minus : LexerToken()              // -
        object Mul : LexerToken()                // *
        object Double_Equals : LexerToken()      // ==

        object And : LexerToken()                // &&
        object Or : LexerToken()                 // ||
        object Not : LexerToken()                // !
        object NotEqual : LexerToken()           // !=
        object Less : LexerToken()               // <
        object LessEqual : LexerToken()          // <=
        object Greater : LexerToken()            // >
        object GreaterEqual : LexerToken()       // >=

        data class Ident(val identify: String) : LexerToken()

        // Literals
        data class Boolean_Literal(val b: Boolean) : LexerToken()
        data class Number_Literal(val n: Int) : LexerToken()
        data class Char_Literal(val c: Char) : LexerToken()
        data class String_Literal(val s: String) : LexerToken()

        // Control Token
        object EOF : LexerToken()
}