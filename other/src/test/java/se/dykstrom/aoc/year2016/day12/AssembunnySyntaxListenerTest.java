package se.dykstrom.aoc.year2016.day12;

import org.antlr.v4.runtime.*;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static se.dykstrom.aoc.year2016.day12.Register.*;

public class AssembunnySyntaxListenerTest {

    @Test
    public void shouldParseOneDec() {
        Instruction dec = new Dec(B);
        assertEquals(singletonList(dec), parse("dec b"));
    }

    @Test
    public void shouldParseOneInc() {
        Instruction inc = new Inc(C);
        assertEquals(singletonList(inc), parse("inc c"));
    }

    @Test
    public void shouldParseIncDec() {
        Instruction inc = new Inc(C);
        Instruction dec = new Dec(D);
        assertEquals(asList(inc, dec), parse("inc c\ndec d"));
    }

    @Test
    public void shouldParseOneCpyFromInteger() {
        Instruction cpy = new CpyFromInteger(5, B);
        assertEquals(singletonList(cpy), parse("cpy 5 b"));
    }

    @Test
    public void shouldParseOneCpyFromRegister() {
        Instruction cpy = new CpyFromRegister(A, B);
        assertEquals(singletonList(cpy), parse("cpy a b"));
    }

    @Test
    public void shouldParseOneJnzFromInteger() {
        Instruction jnz = new JnzOnInteger(5, -5);
        assertEquals(singletonList(jnz), parse("jnz 5 -5"));
    }

    @Test
    public void shouldParseOneJnzFromRegister() {
        Instruction jnz = new JnzOnRegister(A, 0);
        assertEquals(singletonList(jnz), parse("jnz a 0"));
    }

    @Test
    public void shouldParseMixedInstructions() {
        Instruction cpy_1 = new CpyFromInteger(1, C);
        Instruction inc = new Inc(C);
        Instruction dec = new Dec(D);
        Instruction cpy_d = new CpyFromRegister(D, A);
        Instruction jnz_a = new JnzOnRegister(A, -2);
        assertEquals(asList(cpy_1, inc, dec, cpy_d, jnz_a), parse("cpy 1 c\ninc c\ndec d\ncpy d a\njnz a -2"));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidInstruction() {
        parse("jz a 5");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidDecRegister() {
        parse("dec 7");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidIncRegister() {
        parse("inc h");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidCpyDestination() {
        parse("cpy a 5");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidJnzOffset() {
        parse("jnz d a");
    }

    private List<Instruction> parse(String text) {
        AssembunnyLexer lexer = new AssembunnyLexer(new ANTLRInputStream(text));
        lexer.addErrorListener(ERROR_LISTENER);

        AssembunnyParser parser = new AssembunnyParser(new CommonTokenStream(lexer));
        parser.addErrorListener(ERROR_LISTENER);

        AssembunnySyntaxListener listener = new AssembunnySyntaxListener();
        parser.addParseListener(listener);
        parser.program();
        return listener.getInstructions();
    }

    private static final BaseErrorListener ERROR_LISTENER = new BaseErrorListener() {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            throw new IllegalStateException("Syntax error at " + line + ":" + charPositionInLine + ": " + msg, e);
        }
    };
}
