package se.dykstrom.aoc.year2016.day12;

import java.util.Objects;

public class JnzFromInteger extends AbstractJnz {

    private final Integer source;

    public JnzFromInteger(Integer source, Integer offset) {
        super(offset);
        this.source = source;
    }

    @Override
    public String toString() {
        return "jnz " + source + " " + offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        JnzFromInteger that = (JnzFromInteger) obj;
        return Objects.equals(this.source, that.source) && Objects.equals(this.offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, offset);
    }

    @Override
    public void execute() {

    }
}
