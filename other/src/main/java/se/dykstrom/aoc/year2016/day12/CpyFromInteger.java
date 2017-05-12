package se.dykstrom.aoc.year2016.day12;

import lombok.Value;

@Value
public class CpyFromInteger implements Instruction {

    private final Integer source;

    private final Register dest;

    @Override
    public String toString() {
        return "cpy " + source + " " + dest.toString().toLowerCase();
    }

    @Override
    public State execute(State state) {
        return state.with(dest, source);
    }
}
