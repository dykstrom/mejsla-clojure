package se.dykstrom.aoc.year2016.day12;

import lombok.Value;

import static se.dykstrom.aoc.year2016.day12.Register.P;

@Value
public class JnzOnInteger implements Instruction {

    private final Integer source;

    private final Integer offset;

    @Override
    public String toString() {
        return "jnz " + source + " " + offset;
    }

    @Override
    public State execute(State state) {
        if (source != 0) {
            return state.with(P, state.get(P) + offset - 1);
        } else {
            return state;
        }
    }
}
