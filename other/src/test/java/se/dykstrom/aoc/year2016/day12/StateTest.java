package se.dykstrom.aoc.year2016.day12;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.dykstrom.aoc.year2016.day12.Register.A;
import static se.dykstrom.aoc.year2016.day12.Register.B;
import static se.dykstrom.aoc.year2016.day12.Register.P;

public class StateTest {

	private static final State INITIAL_STATE = State.of(0, 0, 0, 0, 0);

    @Test
    public void shouldUpdateOne() {
        assertEquals(State.of(17, 0, 0, 0, 0), INITIAL_STATE.with(A, 17));
        // Initial state should still be the same
        assertEquals(State.of(0, 0, 0, 0, 0), INITIAL_STATE);
    }

    @Test
    public void shouldUpdateSome() {
        assertEquals(State.of(17, -5, 0, 0, 42), INITIAL_STATE.with(A, 17).with(B, -5).with(P, 42));
        // Initial state should still be the same
        assertEquals(State.of(0, 0, 0, 0, 0), INITIAL_STATE);
    }
}
