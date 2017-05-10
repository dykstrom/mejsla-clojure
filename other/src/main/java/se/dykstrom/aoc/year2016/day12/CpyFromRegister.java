package se.dykstrom.aoc.year2016.day12;

import java.util.Objects;

public class CpyFromRegister extends AbstractCpy {

    private final Character source;

    public CpyFromRegister(Character source, Character dest) {
        super(dest);
        this.source = source;
    }

    @Override
    public String toString() {
        return "cpy " + source + " " + dest;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CpyFromRegister that = (CpyFromRegister) obj;
        return Objects.equals(this.source, that.source) && Objects.equals(this.dest, that.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, dest);
    }

    @Override
    public void execute() {

    }
}
