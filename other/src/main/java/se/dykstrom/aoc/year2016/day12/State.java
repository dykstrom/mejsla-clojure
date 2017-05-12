package se.dykstrom.aoc.year2016.day12;

import com.google.common.collect.ImmutableMap;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

import static se.dykstrom.aoc.year2016.day12.Register.*;

/**
 * Contains the state of the registers.
 */
@Value
public class State {

    private final ImmutableMap<Register, Integer> registers;

    private State(int a, int b, int c, int d, int p) {
        registers = ImmutableMap.of(A, a, B, b, C, c, D, d, P, p);
    }

    private State(ImmutableMap<Register, Integer> registers) {
        this.registers = registers;
    }

    /**
     * Creates a new state instance of the given register values.
     */
    public static State of(int a, int b, int c, int d, int p) {
        return new State(a, b, c, d, p);
    }

    /**
     * Returns a copy of this state with the given {@code register} set to {@code value}.
     *
     * @param register The register to update.
     * @param value The new value of the register.
     * @return A copy of this state, with register set to value.
     */
    public State with(Register register, Integer value) {
        Map<Register, Integer> temp = new HashMap<>(registers);
        temp.put(register, value);
        return new State(ImmutableMap.copyOf(temp));
    }

    public Integer get(Register register) {
        return registers.get(register);
    }

    @Override
    public String toString() {
        return "State:" + registers.toString();
    }
}
