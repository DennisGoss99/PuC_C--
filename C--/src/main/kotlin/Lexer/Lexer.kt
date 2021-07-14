package Lexer

import Lexer.Exceptions.*
import PeekableIterator

open class Lexer(input: String) {

    private val iterator: PeekableIterator<Char> = PeekableIterator(input.iterator())
    private var lookahead: LexerToken? = null

    protected open var currentLineOfCode = 1

    public fun next(): LexerToken{

        lookahead?.let { lookahead = null; return it }
        consumeWhitespace()
        consumeComments()

        if (!iterator.hasNext())
            return LexerToken.EOF

        return when (val c = iterator.next()) {
            '(' -> LexerToken.Lparen(currentLineOfCode)
            ')' -> LexerToken.Rparen(currentLineOfCode)
            '[' -> LexerToken.LBracket(currentLineOfCode)
            ']' -> LexerToken.RBracket(currentLineOfCode)
            '{' -> LexerToken.LCurlyBrace(currentLineOfCode)
            '}' -> LexerToken.RCurlyBrace(currentLineOfCode)
            ';' -> LexerToken.Semicolon(currentLineOfCode)
            ',' -> LexerToken.Comma(currentLineOfCode)

            '+' -> LexerToken.Plus(currentLineOfCode)
            '-' -> LexerToken.Minus(currentLineOfCode)
            '*' -> LexerToken.Mul(currentLineOfCode)
            '=' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.Double_Equals(currentLineOfCode)
                }
                else -> LexerToken.AssignEquals(currentLineOfCode)
            }
            ':' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.Equals(currentLineOfCode)
                }
                else -> throw LexerUnexpectedCharException(currentLineOfCode, c, ':')
            }
            '&' -> when (iterator.peek()) {
                '&' -> {
                    iterator.next()
                    LexerToken.And(currentLineOfCode)
                }
                else -> throw LexerUnexpectedCharException(currentLineOfCode, c,'&')
            }
            '|' -> when (iterator.peek()) {
                '|' -> {
                    iterator.next()
                    LexerToken.Or(currentLineOfCode)
                }
                else -> throw LexerUnexpectedCharException(currentLineOfCode, c,'|')
            }
            '!' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.NotEqual(currentLineOfCode)
                }
                else -> LexerToken.Not(currentLineOfCode)
            }
            '<' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.LessEqual(currentLineOfCode)
                }
                else -> LexerToken.Less(currentLineOfCode)
            }
            '>' -> when (iterator.peek()) {
                '=' -> {
                    iterator.next()
                    LexerToken.GreaterEqual(currentLineOfCode)
                }
                else -> LexerToken.Greater(currentLineOfCode)
            }

            '§' -> if (iterator.peek().isJavaIdentifierStart()) {
                LexerToken.NameIdent(identBase(iterator.next()), currentLineOfCode)
            }else
                throw LexerUnexpectedCharException(currentLineOfCode, c, '§')


            '\'' -> getChar()
            '\"' -> getString()
            else -> when {
                c.isJavaIdentifierStart() -> ident(c)
                c.isDigit() -> number(c)
                else -> throw LexerUnexpectedCharException(currentLineOfCode, c)
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
        return LexerToken.Number_Literal(result.toInt(), currentLineOfCode)
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
            "true" -> LexerToken.Boolean_Literal(true, currentLineOfCode)
            "false" -> LexerToken.Boolean_Literal(false, currentLineOfCode)
            "if" -> LexerToken.If(currentLineOfCode)
            "else" -> LexerToken.Else(currentLineOfCode)
            "while" -> LexerToken.While(currentLineOfCode)
            "return" -> LexerToken.Return(currentLineOfCode)
            "struct" -> LexerToken.Struct(currentLineOfCode)
            else -> {
                if(result[0].isLowerCase())
                    LexerToken.TypeIdent(result, currentLineOfCode)
                else
                    LexerToken.FunctionIdent(result, currentLineOfCode)
            }
        }
    }

    private fun getChar(): LexerToken {

        return when(val c = iterator.peek()){
            '\'' -> throw LexerConstCharException(currentLineOfCode, "Char can't be empty")
            else -> {

                val c = iterator.next()
                if(iterator.next() != '\'')
                    throw LexerConstCharException(currentLineOfCode, "Char can only be a single char")

                return LexerToken.Char_Literal(c, currentLineOfCode);
            }
        }

    }

    private fun getString(): LexerToken {

        return when(val c = iterator.next()){
            '\"' -> LexerToken.String_Literal("", currentLineOfCode);
            else -> {
                var result = c.toString()

                while (iterator.hasNext() && iterator.peek() != '\"') {
                    result += iterator.next()
                }

                if(!iterator.hasNext())
                    throw LexerConstStringException(currentLineOfCode, "Missing closing '\"' char")

                iterator.next()
                return LexerToken.String_Literal(result, currentLineOfCode);
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
            else -> throw LexerUnexpectedCharException(currentLineOfCode, c)
        }

    }

    private fun consumeWhitespace() {
        while (iterator.hasNext()) {
            val nextChar = iterator.peek()

            if(nextChar == '\n')
                currentLineOfCode++

            if (!nextChar.isWhitespace()) break
            iterator.next()
        }
    }

}