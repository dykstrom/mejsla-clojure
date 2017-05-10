package se.dykstrom.aoc.year2016.day12;

import java.util.ArrayList;
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
        if (ctx.getChildCount() > 0) {
            instructions.add(new Dec(ctx.getChild(1).getText().charAt(0)));
        }
    }

    @Override
    public void exitInc(IncContext ctx) {
        if (ctx.getChildCount() > 0) {
            instructions.add(new Inc(ctx.getChild(1).getText().charAt(0)));
        }
    }

    @Override
    public void exitCpy_from_integer(Cpy_from_integerContext ctx) {
        Integer source = Integer.valueOf(ctx.getChild(1).getText());
        Character dest = ctx.getChild(2).getText().charAt(0);
        instructions.add(new CpyFromInteger(source, dest));
    }

    @Override
    public void exitCpy_from_register(Cpy_from_registerContext ctx) {
        Character source = ctx.getChild(1).getText().charAt(0);
        Character dest = ctx.getChild(2).getText().charAt(0);
        instructions.add(new CpyFromRegister(source, dest));
    }

    @Override
    public void exitJnz_from_integer(Jnz_from_integerContext ctx) {
        Integer source = Integer.valueOf(ctx.getChild(1).getText());
        Integer offset = Integer.valueOf(ctx.getChild(2).getText());
        instructions.add(new JnzFromInteger(source, offset));
    }

    @Override
    public void exitJnz_from_register(Jnz_from_registerContext ctx) {
        Character source = ctx.getChild(1).getText().charAt(0);
        Integer offset = Integer.valueOf(ctx.getChild(2).getText());
        instructions.add(new JnzFromRegister(source, offset));
    }
}
