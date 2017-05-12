package se.dykstrom.aoc.year2016.day12;

public enum Register {

    A,
    B,
    C,
    D,
    P;

    public static Register from(Character c) {
        return valueOf(c.toString().toUpperCase());
    }
}
