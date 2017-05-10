package se.dykstrom.aoc.year2016.day12;

import java.util.Objects;

public class Dec implements Instruction {

    private final Character register;

    public Dec(Character register) {
        this.register = register;
    }

    @Override
    public String toString() {
        return "dec " + register;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Dec that = (Dec) obj;
        return Objects.equals(this.register, that.register);
    }

    @Override
    public int hashCode() {
        return Objects.hash(register);
    }

    @Override
    public void execute() {

    }
}
