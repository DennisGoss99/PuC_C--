package Parser

import Lexer.Lexer
import Lexer.LexerToken
import Parser.ParserToken.Type

class Parser(val lexer: Lexer)
{
    fun DoStuff()
    {
        while (true)
        {
            val token = lexer.next()
            val isFinished = token == LexerToken.EOF

            println(token.toString())

            if(isFinished)
            {
                break;
            }

            when(token)
            {
                // Operator
                LexerToken.Plus,
                LexerToken.Minus,
                LexerToken.Mul,
                LexerToken.Double_Equals,
                LexerToken.And,
                LexerToken.Or,
                LexerToken.NotEqual,
                LexerToken.LessEqual,
                LexerToken.GreaterEqual,
                LexerToken.Greater,
                LexerToken.Less,
                LexerToken.Not
                -> OperatorParse(token);

                // Keywords
                LexerToken.If,
                LexerToken.Else,
                LexerToken.Return,
                LexerToken.Struct,
                LexerToken.While
                -> KeyWordParse(token)

                // Symbol
                LexerToken.AssignEquals,
                LexerToken.Equals,
                LexerToken.Semicolon,
                LexerToken.Lparen,
                LexerToken.Rparen,
                LexerToken.LBracket,
                LexerToken.RBracket,
                LexerToken.LCurlyBrace,
                LexerToken.RCurlyBrace,
                LexerToken.Comma
                -> KeyWordParse(token)

                LexerToken.TypeIdent("int")
                -> TypeIdetifyer(Type.Integer)

                LexerToken.TypeIdent("char")
                -> TypeIdetifyer(Type.Char)

                LexerToken.TypeIdent("bool")
                -> TypeIdetifyer(Type.Boolean)

                LexerToken.TypeIdent("float")
                -> TypeIdetifyer(Type.Float)

                //else -> throw java.lang.Exception("Unkown token detected! <$token>")
            }
        }
    }

    fun OperatorParse(currentToken : LexerToken)
    {

    }

    fun KeyWordParse(currentToken : LexerToken)
    {

    }

    fun TypeIdetifyer(type : Type)
    {

    }

    fun FunctionParse(currentToken : LexerToken)
    {

    }










/*









    fun ExpressionParse(): Expression
    {
        return BinaryParse(0)
    }


    fun BinaryParse(minBP: Int): Expression
    {
        var lhs: Expression = parseApplication()

        while (true)
        {
            val operator = OperatorParse() ?: break

            val (leftBP, rightBP) = OperatorBindingPowerCalculate(operator)

            if (leftBP < minBP)
            {
                break
            }

            lexer.next()

            val rhs = BinaryParse(rightBP)

            lhs = Expression.Binary(operator, lhs, rhs)
        }

        return lhs
    }

    private fun OperatorParse(): Operator?
    {
        return when(lexer.peek())
        {
            LexerToken.Plus -> Operator.Plus
            LexerToken.Minus -> Operator.Minus
            LexerToken.Mul -> Operator.Multiply
            LexerToken.Double_Equals -> Operator.Equals
            else -> null
        }
    }

    fun OperatorBindingPowerCalculate(operator: Operator): Pair<Int, Int>
    {
        return when(operator)
        {
            Operator.Equals -> 1 to 2
            Operator.Plus, Operator.Minus -> 3 to 4
            Operator.Multiply -> 5 to 6
            Operator.DoubleEquals -> TODO()
            Operator.And -> TODO()
            Operator.Or -> TODO()
            Operator.Not -> TODO()
            Operator.NotEqual -> TODO()
            Operator.Less -> TODO()
            Operator.LessEqual -> TODO()
            Operator.Greater -> TODO()
            Operator.GreaterEquals -> TODO()
        }
    }

    fun parseApplication(): Expression
    {
        val func = parseAtom()
        val args: MutableList<Expression> = mutableListOf()

        while (true)
        {
            args += tryParseAtom() ?: break
        }

        return args.fold(func) { acc, arg -> Expression.Application(acc, arg) }
    }

    fun parseAtom(): Expression
    {
        return tryParseAtom() ?: throw Exception("Expected expression, but saw unexpected token: ${lexer.peek()}")
    }

    fun tryParseAtom(): Expression?
    {
        return when (val t = lexer.peek())
        {
            is LexerToken.Boolean_Literal -> TypeBooleanParse()
            is LexerToken.Number_Literal -> TypeNumberParse()
            is LexerToken.Ident -> TypeVariableParse()
            is LexerToken.If -> KeyWordIFParse()
            is LexerToken.Lparen -> parseParenthesized()
            is LexerToken.Equals -> parseLet()
            else -> null
        }
    }



    private fun TypeBooleanParse(): Expression {

        val t = expectNext<LexerToken.Boolean_Literal>("boolean literal")

        return Expression.Boolean(t.b)
    }

    private fun TypeNumberParse(): Expression
    {
        val t = expectNext<LexerToken.Number_Literal>("number literal")

        return Expression.Number(t.n)
    }

    private fun TypeVariableParse(): Expression
    {
        val t = expectNext<LexerToken.Ident>("identifier")

        return Expression.Var(t.identify)
    }

    private fun parseParenthesized(): Expression
    {
        expectNext<LexerToken.Lparen>("(")
        val inner = ExpressionParse()

        expectNext<LexerToken.Rparen>(")")

        return inner
    }

    // if true then 3 else 4
    private fun KeyWordIFParse(): Expression.If
    {
        expectNext<LexerToken.If>("if")
        val condition = ExpressionParse()
        expectNext<LexerToken.If>("then")
        val thenBranch = ExpressionParse()
        expectNext<LexerToken.Else>("else")
        val elseBranch = ExpressionParse()
        return Expression.If(condition, thenBranch, elseBranch)
    }











    private fun parseLet(): Expression
    {
        expectNext<LexerToken.Equals>("let")

        val binder = expectNext<LexerToken.Ident>("binder").identify
        expectNext<LexerToken.Equals>("equals")

        val expr = ExpressionParse()
        expectNext<LexerToken.Equals>("in")

        val body = ExpressionParse()

        return Expression.Let(binder, expr, body)
    }






    private inline fun <reified A>expectNext(msg: String): A
    {
        val next = lexer.next()

        if (next !is A)
        {
            throw Exception("Unexpected token: expected $msg, but saw $next")
        }

        return next
    }

 */
}