import Lexer.Lexer
import Lexer.LexerToken
import kotlin.test.assertEquals

public abstract class BaseLexerTest {

    protected fun getLexerOutPutAsList( lexer: Lexer) : List<LexerToken>{
        val returnValue = mutableListOf<LexerToken>()

        while (lexer.peek() != LexerToken.EOF) {
            returnValue.add(lexer.next())
        }
        returnValue.add(lexer.next())

        return returnValue
    }

    fun A() :Int{
        if(true)
            return 1
        return 0
    }

    protected fun assertEqualLexerList( expectedLexerToken : List<LexerToken> , lexer : Lexer) {
        for (lexerToken in expectedLexerToken){
            assertEquals(lexerToken, lexer.next())
        }
    }

}