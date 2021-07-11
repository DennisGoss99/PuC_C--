import Evaluator.Evaluator
import Evaluator.Exceptions.NotFound.VariableNotFoundRuntimeException
import Lexer.Lexer
import Parser.Parser
import Parser.ParserToken.*
import TypeChecker.Exceptions.TypeCheckerVariableNotFoundException
import TypeChecker.TypeChecker
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

class DeepTest {

    private fun executeCode(code : String, args: List<Expression.Value>? = null): ConstantValue? {

        val parserOutput = Parser(Lexer(code)).ParsingStart()

        TypeChecker(parserOutput, args).check()

        return Evaluator().eval(parserOutput,args)?.value

    }


    @Test
    fun simpleTest(){

        val code = """
            
            int Main(){
                return 5;
            }
            
        """.trimIndent()

        assertEquals(ConstantValue.Integer(5) ,executeCode(code))

    }

    @Test
    fun simpleTest2(){

        val code = """
            
            int Main(){
                return 5+5;
            }
            
        """.trimIndent()

        assertEquals(ConstantValue.Integer(10) ,executeCode(code))

    }

    @Test
    fun helloWeltTest(){

        val code = """
            
            void Main(){
                Println("Hallo Welt");            
            }
            
        """.trimIndent()

        assertEquals(null ,executeCode(code))

    }

    @Test
    fun variableTest(){

        val code = """
            
            int Main(){
                int §a = 5;
                return §a + 5;
            }
            
        """.trimIndent()

        assertEquals(ConstantValue.Integer(10) ,executeCode(code))
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

        assertEquals(ConstantValue.Integer(25) ,executeCode(code))
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

        assertEquals(ConstantValue.Integer(14), executeCode(code))
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

        assertEquals(ConstantValue.Integer(5), executeCode(code))
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

        assertEquals(ConstantValue.Integer(12) ,executeCode(code))
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

        assertEquals(ConstantValue.Integer(15) ,executeCode(code))
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

        assertEquals(ConstantValue.Integer(25) ,executeCode(code))
    }

    @Test
    fun simpleMainParameterTest(){

        val code = """
                        
            int Main(int §b)
            {
                return §b;
            }
        """.trimIndent()

        assertEquals(ConstantValue.Integer(10) ,executeCode(code, listOf(Expression.Value(ConstantValue.Integer(10)))))
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

        assertEquals(ConstantValue.Integer(10) ,executeCode(code, listOf(Expression.Value(ConstantValue.Integer(5)))))
    }

    @Test
    fun mathTest(){

        val code = """
            
            int Main(int §b)
            {
                return  4 * ((3 * 4) + 4) - (4 * 5 - 20);
            }
        """.trimIndent()

        assertEquals(ConstantValue.Integer(64) ,executeCode(code, listOf(Expression.Value(ConstantValue.Integer(5)))))
    }

    @Test
    fun mathTest2(){

        val code = """
            
            int Main(int §b)
            {
                return  -(4+2) * (-4);
            }
        """.trimIndent()

        assertEquals(ConstantValue.Integer(24) ,executeCode(code, listOf(Expression.Value(ConstantValue.Integer(5)))))
    }

    @Test
    fun mathTest3(){

        val code = """            
            int Main(){
                // 2576
                return -1 *( - (3*5) - ( 4 + ( 5 * (30-43)))) * (-56)  ;
            }          
        """.trimIndent()

        assertEquals(ConstantValue.Integer(2576), executeCode(code))
    }

    @Test
    fun mathTest4()
    {
        val code = """            
            int Main()
            {
                // -200 * 30 * 3
                return -(20 * 10) * (10 + 20) * 3 ;
            }          
        """.trimIndent()

        assertEquals(ConstantValue.Integer(-18000), executeCode(code))
    }

    @Test
    fun boolTest(){

        val code = """            
            bool Main(){
                return !(!((5 != 6) == true) || !( 6 < 7 || ( true != false)));
            }                 
        """.trimIndent()

        assertEquals(ConstantValue.Boolean(true), executeCode(code))
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

        assertEquals(ConstantValue.Integer(34) ,executeCode(code, listOf(Expression.Value(ConstantValue.Integer(9)))))
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

        assertEquals(ConstantValue.Integer(34) ,executeCode(code, listOf(Expression.Value(ConstantValue.Integer(9)))))
    }

    @Test
    fun shadowingTest(){

        val code = """
            
            
            int Main(){
            
                int §a = 45;
                
                {
                    int §a = §a + 3 
                    return §a;               
                }
            }
            
        """.trimIndent()

        assertEquals(ConstantValue.Integer(48) ,executeCode(code))

    }

    @Test
    fun shadowingTest2(){

        val code = """
            
            
            int Main(){
            
                int §a = 45;
                
                {
                    int §a = 3;        
                }
                return §a; 
            }
            
        """.trimIndent()

        assertEquals(ConstantValue.Integer(45) ,executeCode(code))

    }

    @Test
    fun shadowingTest3(){

        val code = """
            
            
            int Main(){
            
                int §a = 45;
                
                {
                    int §a = 3;        
                    int §b = 5;
                }
                return §b; 
            }
            
        """.trimIndent()

        assertFailsWith<TypeCheckerVariableNotFoundException> {executeCode(code)}

    }

    @Test
    fun shadowingTest4(){

        val code = """
            
            
            int Main(){
            
                int §a = 1;
                
                {
                    int §a = §a + §a;        
                    int §b = §a + 3;
                    {
                        int §a = §b - 3;
                        return §a;    
                    }
                }
            }
            
        """.trimIndent()

        assertEquals(ConstantValue.Integer(2) ,executeCode(code))

    }

    @Test
    fun moduloTest4(){

        val code = """
            // Pow = n^k;
            int Pow(int §n, int §k){
                int §returnValue = 1;
                
                if(§k == 0){
                    return 1;
                }
                
                while(§k >= 1){
                    §returnValue = §returnValue * §n;
                    §k = §k - 1;
                }
                
                return §returnValue;           
            }
            
            // ModResult = n % k; 
            int Mod(int §n, int §k){
                while(§n - §k > 0){
                    §n = §n - §k;
                }
                return §n;
            }
            
            int Main(){
                return Mod(Pow(5,10),7);
            }
            
        """.trimIndent()

        assertEquals(ConstantValue.Integer(2) ,executeCode(code))
    }

    @Test
    fun primeHowManyTest()
    {
        val code = """
         int Mod(int §n, int §k)
         {
            while(§n >= §k)
            {
                §n = §n - §k;
            }
            return §n;
        }
            
        int Main()
        {
            int §x = 2;
            int §i = 2;
            bool §quitFlag = true;
            int §foundPrimes = 0;
                  
            while(§x <= 1000)
            {                  
                while(§i <= §x && §quitFlag)
                {
                    if((Mod(§x,§i) == 0) && (§x != §i))
                    {
                        §quitFlag = false; // break
                    }
                    else
                    {
                        if(§i == §x)
                        {
                            §foundPrimes = §foundPrimes + 1;
                            //Println(§i);
                        }
                    }     
                                   
                    §i = §i + 1;   
                }
                §quitFlag = true;
                §i = 2;
                §x = §x + 1;
            }                  
            return §foundPrimes;
        }
        
        """.trimIndent()

        assertEquals(ConstantValue.Integer(168) ,executeCode(code))
    }
}