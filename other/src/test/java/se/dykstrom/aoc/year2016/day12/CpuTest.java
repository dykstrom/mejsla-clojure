package se.dykstrom.aoc.year2016.day12;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static se.dykstrom.aoc.year2016.day12.Register.A;

public class CpuTest {

    private final Cpu cpu = new Cpu(Cpu.INITIAL_STATE);

    @Test
    public void shouldExecuteOneInc() {
        List<Instruction> instructions = Collections.singletonList(new Inc(A));
        assertEquals(State.of(1, 0, 0, 0, 1), cpu.execute(instructions));
    }

    @Test
    public void shouldExecuteExample() {
        List<Instruction> instructions = asList(
                new CpyFromInteger(41, A),
                new Inc(A),
                new Inc(A),
                new Dec(A),
                new JnzOnRegister(A, 2),
                new Dec(A)
        );
        assertEquals(42, cpu.execute(instructions).get(A).intValue());
    }
}
