package se.dykstrom.aoc.year2016.day12;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.dykstrom.aoc.year2016.day12.Register.C;

public class PriTest {

    private static final State INITIAL_STATE = State.of(0, 0, 0, 0, 0);

    @Test
    public void shouldNotChangeState() {
        Instruction pri = new Pri(C);
        assertEquals(State.of(0, 0, 0, 0, 0), pri.execute(INITIAL_STATE));
    }
}
