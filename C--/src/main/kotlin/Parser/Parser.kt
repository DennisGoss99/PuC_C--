package Parser

import Lexer.Lexer
import Lexer.LexerToken
import Parser.ParserToken.*

class Parser(val lexer: Lexer) {
    private val aplTree: Declaration.FunctionDeclare? = null

    private inline fun <reified A> FetchNextExpectedToken(expectedType: String): A {
        val next = GetTextToken()

        if (next !is A) {
            throw Exception("Unexpected token: expected $expectedType, but got $next")
        }

        return next
    }

    fun GetTextToken() : LexerToken
    {
        val token = lexer.next()

        println(token.toString())

        return token
    }

    fun ParsingStart() : Declaration.FunctionDeclare
    {
        println("-----<Starting Parsing>-----")

        val functionMain = FuncitonParse()

        if(lexer.peek() is LexerToken.EOF)
        {
            val token = GetTextToken()
            println("-----<Parsing OK>-----")
        }
        else
        {
            println("-----<Missed Tokens>-----")

            while(true)
            {
                val token = GetTextToken()

                if(token is LexerToken.EOF)
                {
                    break;
                }
            }
        }

        return functionMain
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


    fun NameParse(): String
    {
        val name = FetchNextExpectedToken<LexerToken.TypeIdent>("name")

        return name.identify
    }

    fun LocalVariablesParse() : List<Declaration.VariableDeclaration>?
    {
        val localVariableList = mutableListOf<Declaration.VariableDeclaration>()

        val token = lexer.peek()

        if(token is LexerToken.Return)
        {
            return null
        }

        val type = TypeParse()
        val name = NameParse()
        val expression = ExpressionParse()
        val variableDeclaration = Declaration.VariableDeclaration(type, name, expression)

        localVariableList.add(variableDeclaration)

        if(localVariableList.isEmpty())
        {
            return null
        }

        return localVariableList
    }

    fun StatementListParse(): List<Statement>
    {
        val statementList = mutableListOf<Statement>()

        while(true)
        {
            val token = lexer.peek()
            val searchNextExpression = token is LexerToken.Semicolon

            if(searchNextExpression)
            {
                GetTextToken()
                break;
            }

            statementList.add(StatementParse())
        }

        return statementList
    }

    fun BodyParse(): Body
    {
        val expectedLCurly = FetchNextExpectedToken<LexerToken.LCurlyBrace>("LCurlyBrace")
        val localVariables = LocalVariablesParse()
        val statementList = StatementListParse()
        val expectedRCurly = FetchNextExpectedToken<LexerToken.RCurlyBrace>("RCurlyBrace")

        return Body(statementList, localVariables)
    }

    fun ParameterParse(): List<Parameter>? {
        val parameterList = mutableListOf<Parameter>()
        val expectedLBrace = FetchNextExpectedToken<LexerToken.Lparen>("name")

        while (true) {
            val token = lexer.peek()

            if (token is LexerToken.Rparen) {
                GetTextToken()
                break;
            }
        }

        if (parameterList.size == 0) {
            return null
        }

        return parameterList
    }

    fun FuncitonParse(): Declaration.FunctionDeclare
    {
        val type = TypeParse()
        val name = NameParse()
        val parameter = ParameterParse()
        val body = BodyParse()

        return Declaration.FunctionDeclare(type, name, body,parameter)

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

    fun OperatorParse() : Operator
    {
        val token = GetTextToken()

        return when(token)
        {
            is LexerToken.Plus -> Operator.Plus
            is LexerToken.Minus-> Operator.Minus
            is LexerToken.Mul-> Operator.Multiply
            is LexerToken.Double_Equals-> Operator.DoubleEquals

            is LexerToken.And-> Operator.And
            is LexerToken.Or-> Operator.Or
            is LexerToken.Not-> Operator.Not
            is LexerToken.NotEqual-> Operator.NotEqual
            is LexerToken.Less-> Operator.Less
            is LexerToken.LessEqual-> Operator.LessEqual
            is LexerToken.Greater-> Operator.Greater
            is LexerToken.GreaterEqual -> Operator.GreaterEquals
            else-> throw Exception("Invalid/Unkown Operator.")
        }
    }

    fun CalulationParse(leftExpression : Expression) : Expression.Calculation
    {
        val operator = OperatorParse()
        val rightExpression = ExpressionParse()

        return Expression.Calculation(operator, leftExpression, rightExpression)
    }

    fun CalulationParse() : Expression.Calculation
    {
        val leftExpression = ExpressionParse()
        val operator = OperatorParse()
        val rightExpression = ExpressionParse()

        return Expression.Calculation(operator, leftExpression, rightExpression)
    }

    fun ValueParse() : Expression.Value
    {
        val token = GetTextToken()
        val numberLiteral : LexerToken.Number_Literal = token as LexerToken.Number_Literal;

        if(numberLiteral.n is Int)
        {
            return Expression.Value(ConstantValue.ConstInteger(numberLiteral.n, Type.Integer))
        }

       throw java.lang.Exception("Unkown Value Type")
    }

    fun FunctionCallParse() : Expression.FunctionCall
    {
        val name = NameParse()
        val parameter = emptyList<Expression>()

        return Expression.FunctionCall(name, parameter)
    }

    fun UseVariableParse() : Expression.UseVariable
    {
        val name = NameParse()

        return Expression.UseVariable(name)
    }

    fun ExpressionParse(): Expression
    {
        var nextToken = lexer.peek()

        var expression = when(nextToken)
        {
            is LexerToken.Number_Literal -> ValueParse()
            is LexerToken.FunctionIdent -> FunctionCallParse()
            else -> throw Exception("Invalid Expression");
        }

        nextToken = lexer.peek()

        if(nextToken !is LexerToken.Semicolon)
        {
            expression = CalulationParse(expression)
        }

        return expression
    }

    fun AssignmentParse() : Statement.AssignValue
    {
        val type = TypeParse()
        val expression = ExpressionParse()

        return Statement.AssignValue(type, expression)
    }

    fun BlockParse() : Statement.Block
    {
        val body = BodyParse()

        return Statement.Block(body)
    }

    fun WhileParse() : Statement.While
    {
        val condition = ConditionParse()
        val body = BodyParse()

        return  Statement.While(condition, body)
    }

    fun IfParse() : Statement.If
    {
        val condition = ConditionParse()
        val ifBody = BodyParse()
        var elseBody : Body? = null

        val expectedElseToken = lexer.peek()

        if(expectedElseToken is LexerToken.Else)
        {
            GetTextToken()
            elseBody = BodyParse()
        }

        return Statement.If(condition, ifBody, elseBody)
    }

    fun ConditionParse() : Expression
    {
        val variableType = FetchNextExpectedToken<LexerToken.Lparen>("LexerToken.TypeIdent")
        val nextToken = GetTextToken()

        if(nextToken is LexerToken.Rparen)
        {
            throw Exception("Condition can't be empty")
        }

        return ExpressionParse()
    }

    fun StatementParse(): Statement
    {
        val token = lexer.peek()

        if(token is LexerToken.If)
        {
            return IfParse()
        }

        if(token is LexerToken.While)
        {
            return WhileParse()
        }

        if(token is LexerToken.LCurlyBrace)
        {
            return BlockParse()
        }

        if(token is LexerToken.Return)
        {
            return AssignmentParse()
        }

        throw Exception("Statement Parsing failure. Unexpected Token <$token>")
    }

    fun TypeParse(): Type
    {
        val variableType = GetTextToken()

        if(variableType == LexerToken.Return)
        {
            return Type.Return
        }

        val idintifer = variableType as LexerToken.TypeIdent

        return when (idintifer.identify)
        {
            "int" -> Type.Integer
            "bool" -> Type.Boolean
            "char" -> Type.Char
            "string" -> Type.String
            "float" -> Type.Float
            "double" -> Type.Double
            "return" -> Type.Return
            else -> throw Exception("Invalid Type is not known <$variableType>.")
        }
    }

    fun VariableDeclaration(): Declaration.VariableDeclaration
    {
        val type = TypeParse()
        val variableName = FetchNextExpectedToken<LexerToken.TypeIdent>("LexerToken.TypeIdent as FunctionName")
        val expression = ExpressionParse()

        return Declaration.VariableDeclaration(type, variableName.identify, expression)
    }

    fun TypeDeclaration() {
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