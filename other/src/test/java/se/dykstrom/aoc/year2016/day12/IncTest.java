package se.dykstrom.aoc.year2016.day12;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.dykstrom.aoc.year2016.day12.Register.*;

public class IncTest {

    private static final State INITIAL_STATE = State.of(0, 0, 0, 0, 0);

    @Test
    public void shouldIncrementA() {
        Instruction inc = new Inc(A);
        assertEquals(State.of(1, 0, 0, 0, 0), inc.execute(INITIAL_STATE));
    }

    @Test
    public void shouldIncrementC() {
        Instruction inc = new Inc(C);
        assertEquals(State.of(0, 0, 1, 0, 0), inc.execute(INITIAL_STATE));
    }
}
