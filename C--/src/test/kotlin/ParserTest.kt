import Lexer.Lexer
import Parser.*
import Parser.ParserToken.*
import org.junit.Test
import kotlin.test.assertEquals

class ParserTest
{
    fun CallMain(statementList: List<Statement>): List<Declaration>
    {
        return CallMain(null, null, statementList);
    }

    fun CallMain(localVariable: List<Declaration.VariableDeclaration>?, statementList: List<Statement>): List<Declaration>
    {
        return CallMain(localVariable, null, statementList);
    }

    fun CallMain(localVariables: List<Declaration.VariableDeclaration>?, parameters: List<Parameter>?, statementList: List<Statement>): List<Declaration>
    {
        return listOf<Declaration>(Declaration.FunctionDeclare(
            Type.Integer,
            "Main",
            Body(statementList, localVariables),
            parameters
        ))
    }

    fun TestIfTreeIsAsExpected(code : String, declaration: List<Declaration>)
    {
        println("-----<Code>-----")
        println(code)
        println("----------------")

        val lexer = Lexer(code)
        val parser = Parser(lexer)
        val parserTokenTree = parser.ParsingStart()

        println(declaration)
        println(parserTokenTree)

        assertEquals(declaration.toString(), parserTokenTree.toString())
    }


    @Test
    fun ReturnDirectTest()
    {
        val code = """
            int Main()
            {
                return 5;
            }
        """.trimIndent()

        val statementList = listOf<Statement>(
            Statement.AssignValue("return", Expression.Value(ConstantValue.Integer(5)))
        )

        val tree = CallMain(statementList)

        TestIfTreeIsAsExpected(code, tree);
    }

    @Test
    fun ReturnWithAdditionTest()
    {
        val code = """
            int Main()
            {
                return 5 + 5;
            }
        """.trimIndent()

        val statementList = listOf<Statement>(
            Statement.AssignValue(
                "return",
                Expression.Operation(
                    Operator.Plus,
                    Expression.Value(ConstantValue.Integer(5)),
                    Expression.Value(ConstantValue.Integer(5))
                ))
        )

        val tree = CallMain(statementList)

        TestIfTreeIsAsExpected(code, tree);
    }

    @Test
    fun ReturnWithDeclarationTest()
    {
        val code = """
            int Main()
            {
                int §a = 0;
                
                return §a;
            }
        """.trimIndent()

        val localVariables = listOf<Declaration.VariableDeclaration>(
            Declaration.VariableDeclaration(Type.Integer, "§a", Expression.Value(ConstantValue.Integer(0)))
        )

        val statementList = listOf<Statement>(
            Statement.AssignValue(
                "return",
                Expression.UseVariable("§a")
            )
        )

        val tree = CallMain(localVariables, statementList)

        TestIfTreeIsAsExpected(code, tree)
    }

    @Test
    fun ReturnWithLoopTest()
    {
        val code = """
            int Main()
            {
                int §a = 1;
                
                while(§a == 5)
                {
                    §a = §a + 5;
                }
                
                return §a;
            }
        """.trimIndent()

        val localVariables = listOf<Declaration.VariableDeclaration>(
            Declaration.VariableDeclaration(Type.Integer, "§a", Expression.Value(ConstantValue.Integer(1)))
        )

        val statementList = listOf<Statement>(
            Statement.While(
                Expression.Operation(
                    Operator.DoubleEquals,
                    Expression.UseVariable("§a"),
                    Expression.Value(ConstantValue.Integer(5))
                ),
                Body(
                    listOf<Statement>(
                    Statement.AssignValue(
                        "§a",
                        Expression.Operation(
                            Operator.Plus,
                            Expression.UseVariable("§a"),
                            Expression.Value(ConstantValue.Integer(5))
                        )
                    )
                 )
                )
            ),
            Statement.AssignValue(
                "return",
                Expression.UseVariable("§a")
            )
        )

        val tree = CallMain(localVariables, statementList)

        TestIfTreeIsAsExpected(code, tree)
    }

    //@Test
    fun ReturnWithParametersTest()
    {
        val code = """
            int Main(int §a, int §b)
            {
                return §a * §b;
            }
        """.trimIndent()

        val parameters = listOf<Parameter>(
            Parameter("§a", Type.Integer),
            Parameter("§b", Type.Integer)
        )

        val statementList = listOf<Statement>(
            Statement.AssignValue(
                "return",
                Expression.Operation(
                    Operator.Multiply,
                    Expression.UseVariable("§a"),
                    Expression.UseVariable("§b")
                )
            )
        )

        val tree = CallMain(null, parameters, statementList)

        TestIfTreeIsAsExpected(code, tree)
    }

    @Test
    fun ReturnWithFuncitonCallTest()
    {
        val code = """
            int Main()
            {
                return 2 + A(3,5);
            }
        """.trimIndent()

        val statementList = listOf<Statement>(
            Statement.AssignValue(
                "return",
                Expression.Operation(
                    Operator.Plus,
                    Expression.Value(ConstantValue.Integer(2)),
                    Expression.FunctionCall(
                        "A",
                        listOf<Expression>(
                            Expression.Value(ConstantValue.Integer(3)),
                            Expression.Value(ConstantValue.Integer(5))
                        )
                    )
                )
            )
        )

        val tree = CallMain(null, null, statementList)

        TestIfTreeIsAsExpected(code, tree)
    }

    @Test
    fun ReturnWithIFTest()
    {
        val code = """
            int Main()
            {
                int §w = 3;
                bool §f = §w <= 3;
                
                if(§f)
                {
                    return 1;
                }
            
                return 0;
            }
        """.trimIndent()

        val localVariables = listOf<Declaration.VariableDeclaration>(
            Declaration.VariableDeclaration(Type.Integer, "§w", Expression.Value(ConstantValue.Integer(3))),
            Declaration.VariableDeclaration(Type.Boolean, "§f", Expression.Operation(Operator.LessEqual, Expression.UseVariable("§w"), Expression.Value(ConstantValue.Integer(3))))
        )

        val statementList = listOf<Statement>(
            Statement.If(
                Expression.UseVariable("§f"),
                Body(listOf<Statement>(
                    Statement.AssignValue(
                        "return",
                        Expression.Value(ConstantValue.Integer(1))
                    )
                )),
                null
            ),
            Statement.AssignValue(
                "return",
                Expression.Value(ConstantValue.Integer(0))
            )

        )

        val tree = CallMain(localVariables, statementList)

        TestIfTreeIsAsExpected(code, tree)
    }
}