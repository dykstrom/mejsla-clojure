package se.dykstrom.aoc.year2016.day12;

abstract class AbstractJnz implements Instruction {

    protected final Integer offset;

    AbstractJnz(Integer offset) {
        this.offset = offset;
    }
}
