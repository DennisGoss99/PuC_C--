import Evaluator.Evaluator
import Lexer.Lexer
import Lexer.LexerToken
import Parser.Parser
import Parser.ParserToken.*
import TypeChecker.TypeChecker

private fun executeCode(code : String, args: List<Expression.Value>? = null): ConstantValue? {

    val parserOutput = Parser(Lexer(code)).ParsingStart()

    TypeChecker(parserOutput, args).check()

    return Evaluator().eval(parserOutput,args)?.value

}
fun main(){

    val code = """
            string Hallo(int §a, string §b){
            return "";
            }
            
            string Main(){
                string §a = ToString(54);
            
            
                return Hallo(4545,§a + ToString(50));
            }
            
        """.trimIndent()

    executeCode(code);

}