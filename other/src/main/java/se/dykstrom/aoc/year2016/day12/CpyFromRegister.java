package se.dykstrom.aoc.year2016.day12;

import lombok.Value;

@Value
public class CpyFromRegister implements Instruction {

    private final Register source;

    private final Register dest;

    @Override
    public String toString() {
        return "cpy " + source.toString().toLowerCase() + " " + dest.toString().toLowerCase();
    }

    @Override
    public State execute(State state) {
        return state.with(dest, state.get(source));
    }
}
