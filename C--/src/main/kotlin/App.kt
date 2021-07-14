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
        
        
        
            int Main(char §a){
                string §a = ToString(54);
    
                return 50;
            }
            
        """.trimIndent()

    println(executeCode(code))

}