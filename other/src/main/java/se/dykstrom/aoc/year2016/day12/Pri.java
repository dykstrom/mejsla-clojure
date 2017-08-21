package se.dykstrom.aoc.year2016.day12;

import lombok.Value;

@Value
public class Pri implements Instruction {

    private final Register register;

    @Override
    public String toString() {
        return "pri " + register.toString().toLowerCase();
    }

    @Override
    public State execute(State state) {
        System.out.println(state.get(register));
        return state;
    }
}
