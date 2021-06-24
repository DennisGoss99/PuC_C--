import Lexer.Lexer
import Lexer.LexerToken
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LexerTest : BaseLexerTest() {

    @Test
    fun simpleLexerTest(){

        val code = """
            int b = 5;
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            LexerToken.Ident("int"),
            LexerToken.Ident("b"),
            LexerToken.Equals,
            LexerToken.Number_Literal(5),
            LexerToken.Semicolon,
            LexerToken.EOF,
            LexerToken.EOF
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)

    }

    @Test
    fun simpleCommentLexerTest(){

        val code = """
            //int b = 5;
        """.trimIndent()

        val lexer = Lexer(code);

        assertEquals(LexerToken.EOF,lexer.next())
    }

    @Test
    fun ifLexerTest(){

        val code = """
            if(true){
                int b = 123;
            }else{
                int baaaFF = 523;
            }
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            //if(true){
            LexerToken.If,
            LexerToken.Lparen,
            LexerToken.Boolean_Literal(true),
            LexerToken.Rparen,
            LexerToken.LCurlyBrace,
            // int b = 123;
            LexerToken.Ident("int"),
            LexerToken.Ident("b"),
            LexerToken.Equals,
            LexerToken.Number_Literal(123),
            LexerToken.Semicolon,
            // }else{
            LexerToken.RCurlyBrace,
            LexerToken.Else,
            LexerToken.LCurlyBrace,
            // int baaaFF = 523;
            LexerToken.Ident("int"),
            LexerToken.Ident("baaaFF"),
            LexerToken.Equals,
            LexerToken.Number_Literal(523),
            LexerToken.Semicolon,
            // }
            LexerToken.RCurlyBrace,
            LexerToken.EOF
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)
    }

    @Test
    fun advancedIfLexerTest(){

        val code = """
            int i = 5;
            bool b = 5<8;
            if(i >= 7 && b || true ){
                // NICE
            }
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            // int i = 5;
            LexerToken.Ident("int"),
            LexerToken.Ident("i"),
            LexerToken.Equals,
            LexerToken.Number_Literal(5),
            LexerToken.Semicolon,
            // int i = 5;
            LexerToken.Ident("bool"),
            LexerToken.Ident("b"),
            LexerToken.Equals,
            LexerToken.Number_Literal(5),
            LexerToken.Less,
            LexerToken.Number_Literal(8),
            LexerToken.Semicolon,
            //if(i >= 7 && b || true )
            LexerToken.If,
            LexerToken.Lparen,
            LexerToken.Ident("i"),
            LexerToken.GreaterEqual,
            LexerToken.Number_Literal(7),
            LexerToken.And,
            LexerToken.Ident("b"),
            LexerToken.Or,
            LexerToken.Boolean_Literal(true),
            LexerToken.Rparen,
            //{}
            LexerToken.LCurlyBrace,
            LexerToken.RCurlyBrace,
            LexerToken.EOF
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)
    }

    @Test
    fun typeLexerTest(){

        val code = """
            int i = 54534;
            char[] s = "Test";
            bool b = true;
            char c = 'h';
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            LexerToken.Ident("int"),
            LexerToken.Ident("i"),
            LexerToken.Equals,
            LexerToken.Number_Literal(54534),
            LexerToken.Semicolon,

            LexerToken.Ident("char[]"),
            LexerToken.Ident("s"),
            LexerToken.Equals,
            LexerToken.String_Literal("Test"),
            LexerToken.Semicolon,

            LexerToken.Ident("bool"),
            LexerToken.Ident("b"),
            LexerToken.Equals,
            LexerToken.Boolean_Literal(true),
            LexerToken.Semicolon,

            LexerToken.Ident("char"),
            LexerToken.Ident("c"),
            LexerToken.Equals,
            LexerToken.Char_Literal('h'),
            LexerToken.Semicolon,
            LexerToken.EOF,
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)

    }

    @Test
    fun advancedCommentLexerTest(){

        val code = """
        if (false){
            // Huge if true
        6 + 4312;
        // smol
        /* ich
        bin auch
        */
        /* Ich /* bin geschachtelt */ */
        }else{ 3 + (4 + 5);}
        // Test
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            //if(true){
            LexerToken.If,
            LexerToken.Lparen,
            LexerToken.Boolean_Literal(false),
            LexerToken.Rparen,
            LexerToken.LCurlyBrace,
            //6 + 4312;
            LexerToken.Number_Literal(6),
            LexerToken.Plus,
            LexerToken.Number_Literal(4312),
            LexerToken.Semicolon,
            // }else{
            LexerToken.RCurlyBrace,
            LexerToken.Else,
            LexerToken.LCurlyBrace,
            // 3 + (4 + 5);
            LexerToken.Number_Literal(3),
            LexerToken.Plus,
            LexerToken.Lparen,
            LexerToken.Number_Literal(4),
            LexerToken.Plus,
            LexerToken.Number_Literal(5),
            LexerToken.Rparen,
            LexerToken.Semicolon,
            // }
            LexerToken.RCurlyBrace,
            LexerToken.EOF
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)

    }

    @Test
    fun functionLexerTest(){

        val code = """
            int Hallo(int a, char[] b){
            return 0;
            }
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            LexerToken.Ident("int"),
            LexerToken.Ident("Hallo"),
            LexerToken.Lparen,
            LexerToken.Ident("int"),
            LexerToken.Ident("a"),
            LexerToken.Comma,
            LexerToken.Ident("char[]"),
            LexerToken.Ident("b"),
            LexerToken.Rparen,
            LexerToken.LCurlyBrace,
            LexerToken.Return,
            LexerToken.Number_Literal(0),
            LexerToken.Semicolon,
            LexerToken.RCurlyBrace,
            LexerToken.EOF,
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)

    }

    @Test
    fun functionLexerTest2(){

        val code = """
            void Hallo(char[] b){
            b = "TEST";
            }
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            LexerToken.Void,
            LexerToken.Ident("Hallo"),
            LexerToken.Lparen,
            LexerToken.Ident("char[]"),
            LexerToken.Ident("b"),
            LexerToken.Rparen,
            LexerToken.LCurlyBrace,
            LexerToken.Ident("b"),
            LexerToken.Equals,
            LexerToken.String_Literal("TEST"),
            LexerToken.Semicolon,
            LexerToken.RCurlyBrace,
            LexerToken.EOF,
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)

    }

    @Test
    fun structLexerTest(){

        val code = """
            struct adresse {
                char[50] name;
                char[100] strasse;
                int hausnummer;
                int plz;
                char[50] stadt;
            };
        """.trimIndent()

        val lexer = Lexer(code);

        val expectedLexerTokenList = listOf<LexerToken>(
            LexerToken.Struct,
            LexerToken.Ident("adresse"),
            LexerToken.LCurlyBrace,

            LexerToken.Ident("char[50]"),
            LexerToken.Ident("name"),
            LexerToken.Semicolon,

            LexerToken.Ident("char[100]"),
            LexerToken.Ident("strasse"),
            LexerToken.Semicolon,

            LexerToken.Ident("int"),
            LexerToken.Ident("hausnummer"),
            LexerToken.Semicolon,

            LexerToken.Ident("int"),
            LexerToken.Ident("plz"),
            LexerToken.Semicolon,

            LexerToken.Ident("char[50]"),
            LexerToken.Ident("stadt"),
            LexerToken.Semicolon,

            LexerToken.RCurlyBrace,
            LexerToken.Semicolon,
            LexerToken.EOF,
        )

        assertEqualLexerList(expectedLexerTokenList,lexer)

    }

}