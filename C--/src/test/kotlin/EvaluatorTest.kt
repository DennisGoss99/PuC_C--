import Evaluator.Evaluator
import Parser.ParserToken.*
import org.junit.Test
import kotlin.test.assertEquals

class EvaluatorTest {


    @Test
    fun simpleMainTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare
                (
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>
                        (
                        Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Integer(5)))
                    )
                ),
                null
            )
        )

        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(5)),evaluator.eval(declarations,null))

    }

    @Test
    fun simpleAdditionMainTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare
                (
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(
                            Type.Return,
                            Expression.Operation(
                                Operator.Plus,
                                Expression.Value(ConstantValue.Integer(5)),
                                Expression.Value(ConstantValue.Integer(5))
                                )
                        )
                    )
                ),
                null
            )
        )

        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(10)),evaluator.eval(declarations,null))

    }

    @Test
    fun globalVariableTest(){

        val declarations = listOf<Declaration>(
            Declaration.VariableDeclaration(Type.Integer,"§a",Expression.Value(ConstantValue.Integer(5))),
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(Type.Return,Expression.UseVariable("§a")))
                ),
                null
            )
        )


        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(5)),evaluator.eval(declarations,null))

    }

    @Test
    fun sameVariableNameTest(){

        val declarations = listOf<Declaration>(
            Declaration.VariableDeclaration(Type.Integer,"§a",Expression.Value(ConstantValue.Integer(5))),
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(Type.Return,Expression.UseVariable("§a"))),
                    listOf<Declaration.VariableDeclaration>(Declaration.VariableDeclaration(Type.Integer,"§a",Expression.Value(ConstantValue.Integer(15))))
                ),
                null
            )
        )


        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(15)),evaluator.eval(declarations,null))

    }

    @Test
    fun variableAdditionTest(){

        val declarations = listOf<Declaration>(
            Declaration.VariableDeclaration(Type.Integer,"§b",Expression.Value(ConstantValue.Integer(5))),
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(
                            Type.Return,
                            Expression.Operation(
                                Operator.Plus,
                                Expression.UseVariable("§a"),
                                Expression.UseVariable("§b")
                            )
                        )
                    ),
                    listOf<Declaration.VariableDeclaration>(Declaration.VariableDeclaration(Type.Integer,"§a",Expression.Value(ConstantValue.Integer(15))))
                ),
                null
            )
        )


        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(20)),evaluator.eval(declarations,null))

    }

    @Test
    fun reassignVariableAdditionTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(
                            Type.Variable("§a"),
                            Expression.Value(ConstantValue.Integer(20))
                        ),
                        Statement.AssignValue(
                            Type.Return,
                            Expression.UseVariable("§a")
                        )
                    ),
                    listOf<Declaration.VariableDeclaration>(Declaration.VariableDeclaration(Type.Integer,"§a",Expression.Value(ConstantValue.Integer(15))))
                ),
                null
            )
        )


        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(20)),evaluator.eval(declarations,null))

    }

    @Test
    fun simpleIfTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.If(
                            Expression.Value(ConstantValue.Boolean(true)),
                            Body(
                                listOf<Statement>(
                                    Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Integer(10)))
                                )
                            ),
                            null
                        ),
                        Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Integer(5)))
                    ),
                ),
                null
            )
        )

        val declarations2 = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.If(
                            Expression.Value(ConstantValue.Boolean(false)),
                            Body(
                                listOf<Statement>(
                                    Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Integer(10)))
                                )
                            ),
                            null
                        ),
                        Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Integer(5)))
                    ),
                ),
                null
            )
        )


        var evaluator = Evaluator()
        assertEquals(Expression.Value(ConstantValue.Integer(10)),evaluator.eval(declarations,null))

        var evaluator2 = Evaluator()
        assertEquals(Expression.Value(ConstantValue.Integer(5)),evaluator2.eval(declarations2,null))
    }

    @Test
    fun ifTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Boolean,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(
                            Type.Variable("§cc"),
                            Expression.Operation(Operator.Greater, Expression.Operation(Operator.Multiply,Expression.UseVariable("§a"),Expression.Value(ConstantValue.Integer(5))), Expression.UseVariable("§b"))
                        ),
                        Statement.If(
                            Expression.UseVariable("§cc"),
                            Body(
                                listOf<Statement>(
                                    Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Boolean(true)))
                                )
                            ),
                            Body(
                                listOf<Statement>(
                                    Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Boolean(false)))
                                )
                            )
                        ),
                        Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Integer(5)))
                    ),
                    listOf(
                        Declaration.VariableDeclaration(Type.Integer,"§a",Expression.Value(ConstantValue.Integer(5))),
                        Declaration.VariableDeclaration(Type.Integer,"§b",Expression.Value(ConstantValue.Integer(24))),
                        Declaration.VariableDeclaration(Type.Integer,"§cc",Expression.Value(ConstantValue.Boolean(false))),
                    )
                ),
                null
            )
        )

        var evaluator = Evaluator()
        assertEquals(Expression.Value(ConstantValue.Boolean(true)),evaluator.eval(declarations,null))

    }

    @Test
    fun noParameterFunctionTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Integer,
                "A",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(Type.Return,Expression.Value(ConstantValue.Integer(5))))
                ),
                null
            ),
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(Type.Return,Expression.FunctionCall("A",null)))
                ),
                null
            )
        )


        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(5)),evaluator.eval(declarations,null))

    }

    @Test
    fun parameterFunctionTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Integer,
                "A",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(Type.Return,Expression.UseVariable("§hallo")))
                ),
                listOf(Parameter("§hallo",Type.Integer))
            ),
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(Type.Return,Expression.FunctionCall("A", listOf(Expression.Value(ConstantValue.Integer(5))))))
                ),
                null
            )
        )


        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(5)),evaluator.eval(declarations,null))

    }

    @Test
    fun advancedParameterFunctionTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Integer,
                "A",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(Type.Return,
                            Expression.Operation(
                            Operator.Multiply,
                            Expression.Value(ConstantValue.Integer(5)),
                            Expression.UseVariable("§hallo"))
                    )
                    )
                ),
                listOf(Parameter("§hallo",Type.Integer))
            ),
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(
                            Type.Return,
                            Expression.FunctionCall(
                                "A",
                                listOf(
                                    Expression.FunctionCall(
                                        "A",
                                        listOf(Expression.Value(ConstantValue.Integer(
                                            5
                                        )))
                                    )
                                )
                            )
                        )
                    )
                ),
                null
            )
        )

        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(125)),evaluator.eval(declarations,null))

    }

    @Test
    fun whileTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Boolean,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.While(
                            Expression.Operation(Operator.Less,Expression.UseVariable("§a"),Expression.Value(ConstantValue.Integer(100))),
                            Body(
                                listOf<Statement>(
                                    Statement.AssignValue(Type.Variable("§a"),Expression.Operation(Operator.Multiply,Expression.UseVariable("§a"),Expression.UseVariable("§a")))
                                )
                            )
                        ),
                        Statement.AssignValue(Type.Return,Expression.UseVariable("§a"))
                    ),
                    listOf(
                        Declaration.VariableDeclaration(Type.Integer,"§a",Expression.Value(ConstantValue.Integer(2))),
                    )
                ),
                null
            )
        )

        var evaluator = Evaluator()
        assertEquals(Expression.Value(ConstantValue.Integer(256)),evaluator.eval(declarations,null))

    }

    @Test
    fun FibonacciFunctionTest(){

        val declarations = listOf<Declaration>(
            Declaration.FunctionDeclare(
                Type.Integer,
                "F",
                Body(
                    listOf<Statement>(
                        Statement.If(
                            Expression.Operation(
                                Operator.Or,
                                Expression.Operation(Operator.DoubleEquals, Expression.UseVariable("§n"),Expression.Value(ConstantValue.Integer(0))),
                                Expression.Operation(Operator.DoubleEquals, Expression.UseVariable("§n"),Expression.Value(ConstantValue.Integer(1))),
                            ),
                            Body(
                                listOf<Statement>(
                                    Statement.AssignValue(
                                        Type.Return,
                                        Expression.UseVariable("§n")
                                    )
                                )
                            ),
                            Body(
                                listOf<Statement>(
                                    Statement.AssignValue(
                                        Type.Return,
                                        Expression.Operation(
                                            Operator.Plus,
                                            Expression.FunctionCall("F", listOf(
                                                Expression.Operation(
                                                    Operator.Minus,
                                                    Expression.UseVariable("§n"),
                                                    Expression.Value(ConstantValue.Integer(1))
                                                )
                                            )),
                                            Expression.FunctionCall("F", listOf(
                                                Expression.Operation(
                                                    Operator.Minus,
                                                    Expression.UseVariable("§n"),
                                                    Expression.Value(ConstantValue.Integer(2))
                                                )
                                            ))
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                listOf(Parameter("§n",Type.Integer))
            ),
            Declaration.FunctionDeclare(
                Type.Integer,
                "Main",
                Body(
                    listOf<Statement>(
                        Statement.AssignValue(
                            Type.Return,
                            Expression.FunctionCall(
                                "F",
                                listOf(Expression.Value(ConstantValue.Integer(9)))
                            )
                        )
                    )
                ),
                null
            )
        )

        var evaluator = Evaluator()

        assertEquals(Expression.Value(ConstantValue.Integer(34)),evaluator.eval(declarations,null))

    }

}