import Evaluator.Evaluator
import Lexer.Lexer
import Parser.Parser
import Parser.ParserToken.ConstantValue
import Parser.ParserToken.Expression
import org.junit.Test
import kotlin.test.assertEquals

class DeepTest {

    @Test
    fun simpleTest(){

        val code = """
            
            int Main(){
                return 5;
            }
            
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(5) ,evaluator.eval(parserOutput).value)

    }

    @Test
    fun simpleTest2(){

        val code = """
            
            int Main(){
                return 5+5;
            }
            
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(10) ,evaluator.eval(parserOutput).value)

    }

    @Test
    fun variableTest(){

        val code = """
            
            int Main(){
                int §a = 5;
                return §a + 5;
            }
            
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(10) ,evaluator.eval(parserOutput).value)
    }

    @Test
    fun globalVariableTest(){

        val code = """
            int §a = 5;
                        
            int Main(){
                int §b = 5;
                return §a * §b;
            }
            
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(25) ,evaluator.eval(parserOutput).value)
    }

    @Test
    fun whileLoopTest(){

        val code = """
            int Main()
            {
                int §a = 1;
                
                while(§a != 14)
                {
                    §a = §a + 1;
                }
                
                return §a;
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(14) ,evaluator.eval(parserOutput).value)
    }

    @Test
    fun ifTest(){

        val code = """
            int Main()
            {
                if(true){
                    return 5;
                }else{
                    return 4;
                }  
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(5) ,evaluator.eval(parserOutput).value)
    }

    @Test
    fun simpleFuncTest(){

        val code = """
            
            int A(){
                return 4*3;
            }
            
            int Main()
            {
                return A();
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(12) ,evaluator.eval(parserOutput).value)
    }

    @Test
    fun funcTest(){

        val code = """
            
            int A(int §a){
                return §a+5;
            }
            
            int Main()
            {
                return A(10);
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(15) ,evaluator.eval(parserOutput).value)
    }

    @Test
    fun advancedFuncTest(){

        val code = """
            
            int A(int §a){
                return §a+5;
            }
            
            int Main()
            {
                return A(A(A(10)));
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(25) ,evaluator.eval(parserOutput).value)
    }

    @Test
    fun simpleMainParameterTest(){

        val code = """
                        
            int Main(int §b)
            {
                return §b;
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(10) ,evaluator.eval(parserOutput, listOf(Expression.Value(ConstantValue.Integer(10)))).value)
    }

    @Test
    fun mainParameterTest(){

        val code = """
            
            int A(int §a){
                return §a+5;
            }
            
            int Main(int §b)
            {
                return A(§b);
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(10) ,evaluator.eval(parserOutput, listOf(Expression.Value(ConstantValue.Integer(5)))).value)
    }

    @Test
    fun mathTest(){

        val code = """
            
            int Main(int §b)
            {
                return  4 * ((3 * 4) + 4) - (4 * 5 - 20);
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(64) ,evaluator.eval(parserOutput, listOf(Expression.Value(ConstantValue.Integer(5)))).value)
    }

    @Test
    fun mathTest2(){

        val code = """
            
            int Main(int §b)
            {
                return  -(4+2) * (-4);
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(24) ,evaluator.eval(parserOutput, listOf(Expression.Value(ConstantValue.Integer(5)))).value)
    }

    @Test
    fun recursionTest(){

        val code = """
            
            int F(int §n){
                if(§n == 1){
                    return §n;
                }else{
                    if(§n == 0){
                        return §n;
                    }else{
                        return F(§n - 1) + F(§n - 2);
                    }                   
                }
            }
            
            int Main(int §b)
            {
                return F(§b);
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(34) ,evaluator.eval(parserOutput, listOf(Expression.Value(ConstantValue.Integer(9)))).value)
    }

    @Test
    fun advancedRecursionTest(){

        val code = """
            
            int F(int §n){
                if(§n == 0 || §n == 1){
                    return §n;
                }else{
                    return F(§n - 1) + F(§n - 2)
                }
            }
            
            int Main(int §b)
            {
                return F(§b);
            }
        """.trimIndent()

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        var evaluator = Evaluator()

        assertEquals(ConstantValue.Integer(34) ,evaluator.eval(parserOutput, listOf(Expression.Value(ConstantValue.Integer(9)))).value)
    }

}