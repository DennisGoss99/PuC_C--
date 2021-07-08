package Lexer

import Lexer.Exceptions.*
import PeekableIterator

class Lexer(input: String) {

    private val iterator: PeekableIterator<Char> = PeekableIterator(input.iterator())
    private var lookahead: LexerToken? = null

    public fun next(): LexerToken{

        lookahead?.let { lookahead = null; return it }
        consumeWhitespace()
        consumeComments()

        if (!iterator.hasNext())
            return LexerToken.EOF

        return when (val c = iterator.next()) {
            '(' -> LexerToken.Lparen
            ')' -> LexerToken.Rparen
            '[' -> LexerToken.LBracket
            ']' -> LexerToken.RBracket
            '{' -> LexerToken.LCurlyBrace
            '}' -> LexerToken.RCurlyBrace
            ';' -> LexerToken.Semicolon
            ',' -> LexerToken.Comma

            '+' -> LexerToken.Plus
            '-' -> LexerToken.Minus
            '*' -> LexerToken.Mul
            '=' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.Double_Equals
                }
                else -> LexerToken.AssignEquals
            }
            ':' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.Equals
                }
                else -> throw LexerUnexpectedCharException(c,':')
            }
            '&' -> when (iterator.peek()) {
                '&' -> {
                    iterator.next()
                    LexerToken.And
                }
                else -> throw LexerUnexpectedCharException(c,'&')
            }
            '|' -> when (iterator.peek()) {
                '|' -> {
                    iterator.next()
                    LexerToken.Or
                }
                else -> throw LexerUnexpectedCharException(c,'|')
            }
            '!' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.NotEqual
                }
                else -> LexerToken.Not
            }
            '<' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.LessEqual
                }
                else -> LexerToken.Less
            }
            '>' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.GreaterEqual
                }
                else -> LexerToken.Greater
            }

            '§' -> if (iterator.peek().isJavaIdentifierStart()) {
                LexerToken.NameIdent(identBase(iterator.next()))
            }else
                throw LexerUnexpectedCharException(c,'§')


            '\'' -> getChar()
            '\"' -> getString()
            else -> when {
                c.isJavaIdentifierStart() -> ident(c)
                c.isDigit() -> number(c)
                else -> throw LexerUnexpectedCharException(c)
            }
        }


    }

    public fun peek(): LexerToken {
        val token = next()
        lookahead = token
        return token
    }

    private fun number(c: Char): LexerToken {
        var result = c.toString()
        while (iterator.hasNext() && iterator.peek().isDigit()) {
            result += iterator.next()
        }
        return LexerToken.Number_Literal(result.toInt())
    }

    private fun identBase(c: Char): String {
        var result = c.toString()
        while (iterator.hasNext() && (iterator.peek().isJavaIdentifierPart() || iterator.peek() == '[' || iterator.peek() == ']')) {
            result += iterator.next()
        }
        return result;
    }

    private fun ident(c: Char): LexerToken {
        var result = identBase(c);

        return when (result) {
            "true" -> LexerToken.Boolean_Literal(true)
            "false" -> LexerToken.Boolean_Literal(false)
            "if" -> LexerToken.If
            "else" -> LexerToken.Else
            "while" -> LexerToken.While
            "return" -> LexerToken.Return
            "struct" -> LexerToken.Struct
            else -> {
                if(result[0].isLowerCase())
                    LexerToken.TypeIdent(result)
                else
                    LexerToken.FunctionIdent(result)
            }
        }
    }

    private fun getChar(): LexerToken {

        return when(val c = iterator.peek()){
            '\'' -> throw LexerConstCharException("Char can't be empty")
            else -> {

                val c = iterator.next()
                if(iterator.next() != '\'')
                    throw LexerConstCharException("Char can only be a single char")

                return LexerToken.Char_Literal(c);
            }
        }

    }

    private fun getString(): LexerToken {

        return when(val c = iterator.next()){
            '\"' -> LexerToken.String_Literal("");
            else -> {
                var result = c.toString()

                while (iterator.hasNext() && iterator.peek() != '\"') {
                    result += iterator.next()
                }

                if(!iterator.hasNext())
                    throw LexerConstStringException("Missing closing '\"' char")

                iterator.next()
                return LexerToken.String_Literal(result);
            }
        }

    }

    private fun consumeComments() {
        if(!iterator.hasNext())
            return

        if(iterator.peek() != '/')
            return

        iterator.next()

        when(val c = iterator.next()){
            '/' ->{
                while(iterator.hasNext() && iterator.next() != '\n'){}
                consumeWhitespace()
                consumeComments()
            }
            '*' ->{
                while(iterator.peek() != '*'){
                    if(iterator.peek() == '/' ){
                        consumeComments()
                    }else{
                        iterator.next()
                    }
                }
                iterator.next()
                iterator.next()
                consumeWhitespace()
                consumeComments()
            }
            else -> throw LexerUnexpectedCharException(c)
        }

    }

    private fun consumeWhitespace() {
        while (iterator.hasNext()) {
            val nextChar = iterator.peek()
            if (!nextChar.isWhitespace()) break
            iterator.next()
        }
    }

}