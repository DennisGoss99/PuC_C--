package Parser

import Lexer.Lexer
import Lexer.LexerToken
import Parser.ParserToken.*

class Parser(val lexer: Lexer)
{
    private val aplTree: Declaration.FunctionDeclare? = null

    private inline fun <reified A> FetchNextExpectedToken(expectedType: String): A {
        val next = GetTextToken()

        if (next !is A) {
            throw Exception("Unexpected token: expected $expectedType, but got $next")
        }

        return next
    }

    private fun GetTextToken() : LexerToken
    {
        val token = lexer.next()

        println(token.toString())

        return token
    }

    fun ParsingStart() : List<Declaration> // Return List<Declarations>
    {
        var declarationList : List<Declaration>? = null

        println("-----<Starting Parsing>-----")

        try
        {
            declarationList = DeclarationListParse()
        }
        catch(e : Exception)
        {
            e.printStackTrace()
        }

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

        if(declarationList == null)
        {
            throw Exception("Parsing failed")
        }

        return declarationList
    }

    private fun DeclarationListParse() : List<Declaration>
    {
        val declarationList = mutableListOf<Declaration>()

        while(true)
        {
            val token = lexer.peek()

            if(token is LexerToken.EOF)
            {
                break;
            }

            var isDeclaration = false

            when(token)
            {
                is LexerToken.TypeIdent,
                is LexerToken.FunctionIdent
                -> isDeclaration = true
            }

            if(!isDeclaration)
            {
                throw Exception("Is not a Invalid declaration <$token>.")
            }

            declarationList.add(FuncitonParse())
        }

        return declarationList
    }

    private fun NameParse(): String
    {
        val token = GetTextToken()

        if(token is LexerToken.Return)
        {
            return "return"
        }

        if(token !is LexerToken.NameIdent)
        {
            throw Exception("Inavlid Name")
        }

        val name = token

        return "§${name.identify}" // ???
    }

    private fun LocalVariablesParse() : List<Declaration.VariableDeclaration>?
    {
        val localVariableList = mutableListOf<Declaration.VariableDeclaration>()

        while(true)
        {
            val token = lexer.peek()

            if(token is LexerToken.Return || token !is LexerToken.TypeIdent) // return if there is no data or no "int a" stuff
            {
                break
            }

            val type = TypeParse()
            val name = NameParse()
            val expression = ExpressionParse()
            val variableDeclaration = Declaration.VariableDeclaration(type, name, expression)

            localVariableList.add(variableDeclaration)
        }

        if(localVariableList.isEmpty())
        {
            return null
        }

        return localVariableList
    }

    private fun StatementListParse(): List<Statement>
    {
        val statementList = mutableListOf<Statement>()

        while(true)
        {
            val token = lexer.peek()
            val searchNextExpression = token is LexerToken.RCurlyBrace

            if(searchNextExpression)
            {
                GetTextToken()
                break;
            }

            statementList.add(StatementParse())
        }

        return statementList
    }

    private fun BodyParse(): Body
    {
        val expectedLCurly = FetchNextExpectedToken<LexerToken.LCurlyBrace>("LCurlyBrace")
        val localVariables = LocalVariablesParse()
        val statementList = StatementListParse()
       // val expectedRCurly = FetchNextExpectedToken<LexerToken.RCurlyBrace>("RCurlyBrace")

        return Body(statementList, localVariables)
    }

    private fun ParameterParseAsDeclaration(): List<Parameter>?
    {
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

    private fun FunctionIdentifyParse() : String
    {
        val name = FetchNextExpectedToken<LexerToken.FunctionIdent>("function idefiyer")

        return name.identify
    }

    private fun FuncitonParse(): Declaration.FunctionDeclare
    {
        val type = TypeParse()
        val name = FunctionIdentifyParse()
        val parameter = ParameterParseAsDeclaration()
        val body = BodyParse()

        return Declaration.FunctionDeclare(type, name, body,parameter)
    }

    private fun OperatorParse() : Operator
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
            else-> throw Exception("Invalid/Unkown Operator <$token>.")
        }
    }

    private fun CalulationParse(leftExpression : Expression) : Expression.Operation
    {
        val operator = OperatorParse()
        val rightExpression = ExpressionParse()

        return Expression.Operation(operator, leftExpression, rightExpression)
    }

    private fun CalulationParse() : Expression.Operation
    {
        val leftExpression = ExpressionParse()
        val operator = OperatorParse()
        val rightExpression = ExpressionParse()

        return Expression.Operation(operator, leftExpression, rightExpression)
    }

    private fun ValueParse() : Expression.Value
    {
        val token = GetTextToken()
        val numberLiteral : LexerToken.Number_Literal = token as LexerToken.Number_Literal;

        if(numberLiteral.n is Int)
        {
            return Expression.Value(ConstantValue.Integer(numberLiteral.n, Type.Integer))
        }

       throw java.lang.Exception("Unkown Value Type")
    }

    private fun ParameterParseAsExpression() : List<Expression>?
    {
        val expressionList = mutableListOf<Expression>()
        val leftBracked = FetchNextExpectedToken<LexerToken.Lparen>("'('")

        while(true)
        {
            expressionList.add(ExpressionParse())

            val token = GetTextToken()

            if(token is LexerToken.Rparen)
            {
               break
            }

            if(token !is LexerToken.Comma)
            {
                throw Exception("Invalid expression <$token>")
            }
        }

        if(expressionList.isEmpty())
        {
            return null
        }

        return expressionList
    }

    private fun FunctionCallParse() : Expression.FunctionCall
    {
        val name = FunctionIdentifyParse()
        val parameter = ParameterParseAsExpression()

        return Expression.FunctionCall(name, parameter)
    }

    private fun UseVariableParse() : Expression.UseVariable
    {
        val name = NameParse()

        return Expression.UseVariable(name)
    }

    private fun ExpressionParse(): Expression
    {
        var nextToken = lexer.peek()

        var expression = when(nextToken)
        {
            is LexerToken.Number_Literal -> ValueParse()
            is LexerToken.FunctionIdent -> FunctionCallParse()
            is LexerToken.NameIdent -> Expression.UseVariable(NameParse())

            is LexerToken.AssignEquals -> {
                GetTextToken()
                ExpressionParse()
            }

            else -> throw Exception("Invalid Expression <$nextToken>");
        }

        var expectedSemicolon = lexer.peek()

        when(expectedSemicolon)
        {
            is LexerToken.Semicolon -> GetTextToken()

            is LexerToken.Plus ,
            is LexerToken.Minus ,
            is LexerToken.Mul ,
            is LexerToken.Double_Equals,
            is LexerToken.And ,
            is LexerToken.Or,
            is LexerToken.Not,
            is LexerToken.NotEqual ,
            is LexerToken.Less,
            is LexerToken.LessEqual,
            is LexerToken.Greater,
            is LexerToken.GreaterEqual -> expression = CalulationParse(expression)

        }

        return expression
    }

    private fun AssignmentParse() : Statement.AssignValue
    {
        val type = NameParse()
        val expression = ExpressionParse()

        return Statement.AssignValue(type, expression)
    }

    private fun BlockParse() : Statement.Block
    {
        val body = BodyParse()

        return Statement.Block(body)
    }

    private fun WhileParse() : Statement.While
    {
        val whileTag = FetchNextExpectedToken<LexerToken.While>("While")
        val condition = ConditionParse()
        val body = BodyParse()

        return  Statement.While(condition, body)
    }

    private fun IfParse() : Statement.If
    {
        val ifTag = FetchNextExpectedToken<LexerToken.If>("if")
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

    private fun ConditionParse() : Expression
    {
        val variableType = FetchNextExpectedToken<LexerToken.Lparen>("LexerToken.TypeIdent")
        val nextToken = lexer.peek()

        if(nextToken is LexerToken.Rparen)
        {
            throw Exception("Condition can't be empty")
        }

        val expression = ExpressionParse()

        val rightBrace = FetchNextExpectedToken<LexerToken.Rparen>("Rparen")

        return expression
    }

    private fun AssignParse() : Statement.AssignValue
    {
        val token = NameParse()
        val tokenEquals = FetchNextExpectedToken<LexerToken.AssignEquals>("Equals")
        val expression = ExpressionParse()

        return Statement.AssignValue(token, expression)
    }

    private fun StatementParse(): Statement
    {
        val token = lexer.peek()

        val statement = when(token)
        {
            is LexerToken.If -> IfParse()
            is LexerToken.While -> WhileParse()
            is LexerToken.LCurlyBrace ->  BlockParse()
            is LexerToken.Return -> AssignmentParse()
            is LexerToken.NameIdent -> AssignParse()
            else -> throw Exception("Statement Parsing failure. Unexpected Token <$token>")
        }

        return statement
    }

    private fun TypeParse(): Type
    {
        val variableType = GetTextToken()

        val idintifer = variableType as LexerToken.TypeIdent

        return when (idintifer.identify)
        {
            "int" -> Type.Integer
            "bool" -> Type.Boolean
            "char" -> Type.Char
            "string" -> Type.String
            "float" -> Type.Float
            "double" -> Type.Double
            else -> throw Exception("Invalid Type is not known <$variableType>.")
        }
    }

    private fun VariableDeclaration(): Declaration.VariableDeclaration
    {
        val type = TypeParse()
        val variableName = FetchNextExpectedToken<LexerToken.TypeIdent>("LexerToken.TypeIdent as FunctionName")
        val expression = ExpressionParse()

        return Declaration.VariableDeclaration(type, variableName.identify, expression)
    }
}

/*
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
 */