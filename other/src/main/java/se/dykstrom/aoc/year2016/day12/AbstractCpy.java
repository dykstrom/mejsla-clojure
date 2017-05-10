package se.dykstrom.aoc.year2016.day12;

abstract class AbstractCpy implements Instruction {

    protected final Character dest;

    AbstractCpy(Character dest) {
        this.dest = dest;
    }
}
