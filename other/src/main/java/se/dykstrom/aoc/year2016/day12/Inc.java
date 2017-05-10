package se.dykstrom.aoc.year2016.day12;

import java.util.Objects;

public class Inc implements Instruction {

    private final Character register;

    public Inc(Character register) {
        this.register = register;
    }

    @Override
    public String toString() {
        return "inc " + register;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Inc that = (Inc) obj;
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
