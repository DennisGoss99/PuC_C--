package Parser

import Lexer.Lexer
import Lexer.LexerToken
import Parser.ParserToken.*

class Parser(val lexer: Lexer)
{
    private val aplTree: Declaration.FunctionDeclare? = null

    private inline fun <reified A> FetchNextExpectedToken(expectedType: String): A
    {
        val next = lexer.next()

        if (next !is A)
        {
            throw Exception("Unexpected token: expected $expectedType, but got $next")
        }

        return next
    }


    fun ParsingStart()
    {
        println("Hello")


    }
}

/*

    fun ParsingStart() : Declaration.FunctionDeclare
    {
        while (true)
        {
            val token = lexer.peek()
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
                -> TypeDeclaration()

                LexerToken.TypeIdent("char")
                -> TypeDeclaration()

                LexerToken.TypeIdent("bool")
                -> TypeDeclaration()

                LexerToken.TypeIdent("float")
                -> TypeDeclaration()

                //else -> throw java.lang.Exception("Unkown token detected! <$token>")
            }
        }
    }

    */

/*
    fun NameParse() : String
    {
        val name = FetchNextExpectedToken<LexerToken.TypeIdent>("name")

        return name.identify
    }

    fun StatementListParse() : List<Statement>
    {

    }

    fun BodyParse() : Body
    {
        val expectedLCurly = FetchNextExpectedToken<LexerToken.LCurlyBrace>("name")
        val

        // Parse declartions

        // Parse statements

        return Body()
    }

    fun ParameterParse() : List<Parameter>?
    {
        val parameterList = mutableListOf<Parameter>()
        val expectedLBrace = FetchNextExpectedToken<LexerToken.Lparen>("name")

        while (true)
        {
            val token = lexer.peek()

            if(token is LexerToken.Rparen)
            {
                lexer.next()
                break;
            }
        }

        if(parameterList.size == 0)
        {
            return null
        }

        return parameterList
    }

    fun FuncitonParse() : Declaration.FunctionDeclare
    {
        val type = TypeParse()
        val name = NameParse()
        val parameter = ParameterParse()
        val body = BodyParse()


        return Declaration.FunctionDeclare(type, name, body, parameter)

        /*
        // is This token a ')'
        if(nextToken is LexerToken.Rparen)
        {
            // We have no parameters

        }

        // We have parameters!
        // Could be another declartion

        val lcurlyBrace = FetchNextExpectedToken<LexerToken.LCurlyBrace>("LCurlyBrace")


        val returnToken = FetchNextExpectedToken<LexerToken.Return>("Return")
        */
    }

    fun ExpressionParse() : Expression
    {

    }

    fun StatementParse() : Statement
    {
        val type = TypeParse()
        val expression = ExpressionParse()

        return Statement(type, expression)
    }

    fun TypeParse() : Type
    {
        val variableType = FetchNextExpectedToken<LexerToken.TypeIdent>("LexerToken.TypeIdent")

        return when(variableType.identify)
        {
            "int" -> Type.Integer
            "bool" -> Type.Boolean
            "char" -> Type.Char
            "string" -> Type.String
            "float" -> Type.Float
            "double" -> Type.Double
            else -> throw java.lang.Exception("Invalid Type is not known <$variableType>.")
        }
    }

    fun VariableDeclaration() : Declaration.VariableDeclaration
    {
        val type = TypeParse()
        val variableName = FetchNextExpectedToken<LexerToken.TypeIdent>("LexerToken.TypeIdent as FunctionName")
        val expression = ExpressionParse()

        return Declaration.VariableDeclaration(type, variableName.identify, expression)
    }

    fun TypeDeclaration()
    {
        /*
        val returnType = FetchNextExpectedToken<LexerToken.TypeIdent>("Type Idefifyer")
        val mainName = FetchNextExpectedToken<LexerToken.TypeIdent>("Type Idefifyer")

        // Whats the next token?
        val nextToken = lexer.next()

        // If you find '=', its a variable declaration
        if(nextToken is LexerToken.Equals)
        {
            VariableDeclaration()
        }

        // if you find '(' its a function
        if(nextToken is LexerToken.Lparen)
        {
            FuncitonParse()
        }

        throw Exception("Unexpected Type in TypeDeclaration : should bw '=' or '(' got '$nextToken'")*/
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