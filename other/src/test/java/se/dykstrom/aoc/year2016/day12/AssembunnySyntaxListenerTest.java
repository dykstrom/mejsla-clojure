package se.dykstrom.aoc.year2016.day12;

import org.antlr.v4.runtime.*;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class AssembunnySyntaxListenerTest {

    @Test
    public void shouldParseOneDec() {
        Instruction dec = new Dec('b');
        parseAndAssert("dec b", singletonList(dec));
    }

    @Test
    public void shouldParseOneInc() {
        Instruction inc = new Inc('c');
        parseAndAssert("inc c", singletonList(inc));
    }

    @Test
    public void shouldParseIncDec() {
        Instruction inc = new Inc('c');
        Instruction dec = new Dec('d');
        parseAndAssert("inc c\ndec d", asList(inc, dec));
    }

    @Test
    public void shouldParseOneCpyFromInteger() {
        Instruction cpy = new CpyFromInteger(5, 'b');
        parseAndAssert("cpy 5 b", singletonList(cpy));
    }

    @Test
    public void shouldParseOneCpyFromRegister() {
        Instruction cpy = new CpyFromRegister('a', 'b');
        parseAndAssert("cpy a b", singletonList(cpy));
    }

    @Test
    public void shouldParseOneJnzFromInteger() {
        Instruction jnz = new JnzFromInteger(5, -5);
        parseAndAssert("jnz 5 -5", singletonList(jnz));
    }

    @Test
    public void shouldParseOneJnzFromRegister() {
        Instruction jnz = new JnzFromRegister('a', 0);
        parseAndAssert("jnz a 0", singletonList(jnz));
    }

    @Test
    public void shouldParseMixedInstructions() {
        Instruction cpy_1 = new CpyFromInteger(1, 'c');
        Instruction inc = new Inc('c');
        Instruction dec = new Dec('d');
        Instruction cpy_d = new CpyFromRegister('d', 'a');
        Instruction jnz_a = new JnzFromRegister('a', -2);
        parseAndAssert("cpy 1 c\ninc c\ndec d\ncpy d a\njnz a -2",
                asList(cpy_1, inc, dec, cpy_d, jnz_a));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidInstruction() {
        parse("jz a 5");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidIncRegister() {
        parse("inc h");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotParseInvalidCpyDestination() {
        parse("cpy a 5");
    }

    private void parseAndAssert(String text, List<Instruction> expectedInstructions) {
        assertEquals(expectedInstructions, parse(text));
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
