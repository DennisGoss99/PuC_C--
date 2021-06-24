import Lexer.Lexer
import Lexer.LexerToken

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



    /* Todo:
       1.
       2.
       3. Println Print
       4. Externe Dateien
       5.
       6.
     */



}