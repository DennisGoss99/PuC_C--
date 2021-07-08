import Evaluator.Evaluator
import Lexer.Lexer
import Lexer.LexerToken
import Parser.ParserToken.*

fun test(input: String) {
    println("Lexing: $input")
    val lexer = Lexer(input)
    while (lexer.peek() != LexerToken.EOF) {
        println(lexer.next())
    }
    println(lexer.next())
}

fun main(){

    val code = """
            int Hallo(int a, char[] b){
            return 0;
            }
        """.trimIndent()

    test(code);


//    val declarations = listOf<Declaration>(
//        Declaration.FunctionDeclare(
//            Type.Boolean,
//            "Main",
//            Body(
//                listOf<Statement>(
//                    Statement.While(
//                        Expression.Operation(
//                            Operator.Less,
//                            Expression.UseVariable("§a"),
//                            Expression.Value(ConstantValue.Integer(100))),
//                        Body(
//                            listOf<Statement>(
//                                Statement.AssignValue(
//                                    "§a",
//                                    Expression.Operation(Operator.Plus, Expression.UseVariable("§a"), Expression.Value(ConstantValue.Integer(1)))
//                                ),
//                                Statement.ProcedureCall("Println", listOf(
//                                    Expression.Operation(
//                                        Operator.Plus,
//                                        Expression.Value(ConstantValue.String("Nummer: ")),
//                                        Expression.FunctionCall( "ToString", listOf( Expression.UseVariable("§a"))),
//                                    )
//                                )),
//                                Statement.ProcedureCall("Println", listOf(Expression.Value(ConstantValue.String("------")))))
//                        )
//                    ),
//                    Statement.AssignValue("return", Expression.UseVariable("§a"))
//                ),
//                listOf(
//                    Declaration.VariableDeclaration(Type.Integer,"§a", Expression.Value(ConstantValue.Integer(0))),
//                )
//            ),
//            null
//        )
//    )
//
//    var evaluator = Evaluator()
//    evaluator.eval(declarations,null)




}