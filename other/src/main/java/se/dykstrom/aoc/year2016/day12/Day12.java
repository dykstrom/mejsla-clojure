package se.dykstrom.aoc.year2016.day12;

import org.antlr.v4.runtime.*;

import java.util.List;

import static se.dykstrom.aoc.year2016.day12.Cpu.INITIAL_STATE;
import static se.dykstrom.aoc.year2016.day12.Register.C;

public class Day12 {

    private static final String PUZZLE_INPUT =
            "cpy 1 a\n" +
            "cpy 1 b\n" +
            "cpy 26 d\n" +
            "jnz c 2\n" +
            "jnz 1 5\n" +
            "cpy 7 c\n" +
            "inc d\n" +
            "dec c\n" +
            "jnz c -2\n" +
            "cpy a c\n" +
            "inc a\n" +
            "dec b\n" +
            "jnz b -2\n" +
            "cpy c b\n" +
            "dec d\n" +
            "jnz d -6\n" +
            "cpy 19 c\n" +
            "cpy 14 d\n" +
            "inc a\n" +
            "dec d\n" +
            "jnz d -2\n" +
            "dec c\n" +
            "jnz c -5";

    private static final BaseErrorListener ERROR_LISTENER = new BaseErrorListener() {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            throw new IllegalStateException("Syntax error at " + line + ":" + charPositionInLine + ": " + msg, e);
        }
    };

    private static List<Instruction> parse(String text) {
        AssembunnyLexer lexer = new AssembunnyLexer(new ANTLRInputStream(text));
        lexer.addErrorListener(ERROR_LISTENER);

        AssembunnyParser parser = new AssembunnyParser(new CommonTokenStream(lexer));
        parser.addErrorListener(ERROR_LISTENER);

        AssembunnySyntaxListener listener = new AssembunnySyntaxListener();
        parser.addParseListener(listener);
        parser.program();
        return listener.getInstructions();
    }

    private static State solvePuzzle(State initialState) {
        List<Instruction> instructions = parse(PUZZLE_INPUT);
        Cpu cpu = new Cpu(initialState);
        return cpu.execute(instructions);
    }

    private static void solvePuzzleA() {
        State state = solvePuzzle(INITIAL_STATE);
        System.out.println("Puzzle A, final state: " + state);
    }

    private static void solvePuzzleB() {
        State state = solvePuzzle(INITIAL_STATE.with(C, 1));
        System.out.println("Puzzle B, final state: " + state);
    }

    private static void asm(String code) {
        List<Instruction> instructions = parse(code);
        Cpu cpu = new Cpu(INITIAL_STATE);
        State state = cpu.execute(instructions);
        System.out.println(state);
    }

    public static void main(String[] args) {
        asm("cpy 3 a" +
                "dec a" +
                "jnz a -1");

//        solvePuzzleA();
//        solvePuzzleB();
    }
}
