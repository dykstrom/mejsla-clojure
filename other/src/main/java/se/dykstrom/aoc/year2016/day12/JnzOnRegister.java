package se.dykstrom.aoc.year2016.day12;

import lombok.Value;

import static se.dykstrom.aoc.year2016.day12.Register.P;

@Value
public class JnzOnRegister implements Instruction {

    private final Register source;

    private final Integer offset;

    @Override
    public String toString() {
        return "jnz " + source.toString().toLowerCase() + " " + offset;
    }

    @Override
    public State execute(State state) {
        if (state.get(source) != 0) {
            return state.with(P, state.get(P) + offset - 1);
        } else {
            return state;
        }
    }
}
