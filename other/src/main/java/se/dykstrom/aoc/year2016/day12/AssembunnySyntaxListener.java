package se.dykstrom.aoc.year2016.day12;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static se.dykstrom.aoc.year2016.day12.AssembunnyParser.*;

public class AssembunnySyntaxListener extends AssembunnyBaseListener {

    private final List<Instruction> instructions = new ArrayList<>();

    /**
     * Returns the list of instructions parsed so far.
     */
    List<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public void exitDec(DecContext ctx) {
        // DEC register
        if (valid(ctx.register())) {
            instructions.add(new Dec(getRegister(ctx, 1)));
        }
    }

    @Override
    public void exitInc(IncContext ctx) {
        // INC register
        if (valid(ctx.register())) {
            instructions.add(new Inc(getRegister(ctx, 1)));
        }
    }

    @Override
    public void exitCpyFromInteger(CpyFromIntegerContext ctx) {
        // CPY integer register
        if (valid(ctx.integer()) && valid(ctx.register())) {
            instructions.add(new CpyFromInteger(getInteger(ctx, 1), getRegister(ctx, 2)));
        }
    }

    @Override
    public void exitCpyFromRegister(CpyFromRegisterContext ctx) {
        // CPY register register
        if (valid(ctx.register(0), ctx.register(1))) {
            instructions.add(new CpyFromRegister(getRegister(ctx, 1), getRegister(ctx, 2)));
        }
    }

    @Override
    public void exitJnzOnInteger(JnzOnIntegerContext ctx) {
        // JNZ integer offset
        if (valid(ctx.integer(0)) && valid(ctx.integer(1))) {
            instructions.add(new JnzOnInteger(getInteger(ctx, 1), getInteger(ctx, 2)));
        }
    }

    @Override
    public void exitJnzOnRegister(JnzOnRegisterContext ctx) {
        // JNZ register offset
        if (valid(ctx.register()) && valid(ctx.integer())) {
            instructions.add(new JnzOnRegister(getRegister(ctx, 1), getInteger(ctx, 2)));
        }
    }

    /**
     * Returns {@code true} if all of the {@code RegisterContext}s are valid.
     */
    private boolean valid(RegisterContext... contexts) {
        return Arrays.stream(contexts).allMatch(context -> context != null && context.CHARACTER() != null);
    }

    /**
     * Returns {@code true} if all of the {@code IntegerContext}s are valid.
     */
    private boolean valid(IntegerContext... contexts) {
        return Arrays.stream(contexts).allMatch(context -> context != null && context.NUMBER() != null);
    }

    private Integer getInteger(ParserRuleContext ctx, int i) {
        return Integer.valueOf(ctx.getChild(i).getText());
    }

    private Register getRegister(ParserRuleContext ctx, int i) {
        return Register.from(ctx.getChild(i).getText().charAt(0));
    }
}
