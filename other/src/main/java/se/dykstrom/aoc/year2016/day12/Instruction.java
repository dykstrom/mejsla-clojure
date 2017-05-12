package se.dykstrom.aoc.year2016.day12;

/**
 * Interface to be implemented by all instructions.
 */
public interface Instruction {
    /**
     * Given {@code state}, execute this instruction, and return the modified state.
     *
     * @param state The state before executing the instruction.
     * @return The state after executing the instruction.
     */
    State execute(State state);
}
