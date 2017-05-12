package se.dykstrom.aoc.year2016.day12;

import java.util.List;

import static se.dykstrom.aoc.year2016.day12.Register.P;

/**
 * The CPU maintains the state of the registers, and is able to execute
 * instructions that modify this state.
 */
public class Cpu {

    static final State INITIAL_STATE = State.of(0, 0, 0, 0, 0);

    private State state;

    Cpu(State initialState) {
        this.state = initialState;
    }

    /**
     * Executes the given list of instructions, and returns the resulting state.
     */
    public State execute(List<Instruction> instructions) {
        int size = instructions.size();

        while (state.get(P) < size) {
            // Read instruction
            Instruction instruction = instructions.get(state.get(P));
            // Advance program counter
            state = state.with(P, state.get(P) + 1);
            // Execute instruction
            state = instruction.execute(state);
        }

        return state;
    }
}
