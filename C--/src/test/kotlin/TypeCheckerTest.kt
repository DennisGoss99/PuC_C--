import Evaluator.Evaluator
import Lexer.Lexer
import Parser.Parser
import Parser.ParserToken.*
import TypeChecker.Exceptions.TypeCheckerReturnTypeException
import TypeChecker.TypeChecker
import org.junit.Test
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class TypeCheckerTest {

    private fun parseCode(code : String): List<Declaration> {
        return Parser(Lexer(code)).ParsingStart()
    }

    @Test
    fun simpleTest(){

        val code = """
            
            int Main(){
                return 5;
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun simpleTest2(){

        val code = """
            
            int Main(){
                return 5 + 2;
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun simpleTest3(){

        val code = """
                   
            string Main(){
                return "5" + "2";
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun variableTest(){

        val code = """  
            int Main(){
                int §a = 56 * 2;
                return -(§a + §a);
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun variableTest2(){

        val code = """  
            int Main(){
                int §a = 56 * 2;
                {
                    int §a = 4;
                    return §a;
                }
                return -(§a + §a);
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun globalVariableTest(){

        val code = """  
            int §b = 5;            
            int Main(){
                int §a = 56 * 2;
                return -(§a + §b);
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun globalVariableTest2(){

        val code = """  
            int §b = 5;   
                     
            int A(int §a, int §b){
             return §a + §b;
            } 
                     
            int Main(){
                int §a = 56 * 2;
                return -(§a + §b);
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun functionTest(){

        val code = """
            
            int A(){
                return 5;
            }       
                   
            int Main(){
                return A();
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun functionParameterTest(){

        val code = """
            
            int A(int §a, int §b){
                return §a + §b;
            }       
                   
            int Main(){
                return A(45, 34);
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
    }

    @Test
    fun functionParameterTest2(){

        val code = """
            
            int A(int §a, int §b){
                return "test";
            }       
                   
            int Main(){
                return A(45, 34);
            }
            
        """.trimIndent()

        assertFailsWith<TypeCheckerReturnTypeException> { TypeChecker(parseCode(code)).check() }
    }

    @Test
    fun advancedFunctionTest(){

        val code = """
            
            int Mod(int §n, int §k){
                while(§n - §k > 0){
                    §n = §n - §k;
                }
                return §n;
            }       
                   
            int Main(){
                return Mod(45,16);
            }
            
        """.trimIndent()

        TypeChecker(parseCode(code)).check()
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


        TypeChecker(parseCode(code), listOf(Expression.Value(ConstantValue.Integer(9)))).check()
    }

}