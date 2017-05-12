package se.dykstrom.aoc.year2016.day12;

import lombok.Value;

@Value
public class Dec implements Instruction {

    private final Register register;

    @Override
    public String toString() {
        return "dec " + register.toString().toLowerCase();
    }

    @Override
    public State execute(State state) {
        return state.with(register, state.get(register) - 1);
    }
}
