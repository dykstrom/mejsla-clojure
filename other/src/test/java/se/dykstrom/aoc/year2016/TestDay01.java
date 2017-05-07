package se.dykstrom.aoc.year2016;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static se.dykstrom.aoc.year2016.Day01.distance;
import static se.dykstrom.aoc.year2016.Day01.distanceFromFirstRevisitedPosition;

public class TestDay01 {

    private static final List<String> PUZZLE_INPUT = asList(
            "L4", "L1", "R4", "R1", "R1", "L3", "R5", "L5", "L2", "L3", "R2", "R1", "L4", "R5", "R4", "L2", "R1", "R3",
            "L5", "R1", "L3", "L2", "R5", "L4", "L5", "R1", "R2", "L1", "R5", "L3", "R2", "R2", "L1", "R5", "R2", "L1",
            "L1", "R2", "L1", "R1", "L2", "L2", "R4", "R3", "R2", "L3", "L188", "L3", "R2", "R54", "R1", "R1", "L2", "L4",
            "L3", "L2", "R3", "L1", "L1", "R3", "R5", "L1", "R5", "L1", "L1", "R2", "R4", "R4", "L5", "L4", "L1", "R2",
            "R4", "R5", "L2", "L3", "R5", "L5", "R1", "R5", "L2", "R4", "L2", "L1", "R4", "R3", "R4", "L4", "R3", "L4",
            "R78", "R2", "L3", "R188", "R2", "R3", "L2", "R2", "R3", "R1", "R5", "R1", "L1", "L1", "R4", "R2", "R1", "R5",
            "L1", "R4", "L4", "R2", "R5", "L2", "L5", "R4", "L3", "L2", "R1", "R1", "L5", "L4", "R1", "L5", "L1", "L5",
            "L1", "L4", "L3", "L5", "R4", "R5", "R2", "L5", "R5", "R5", "R4", "R2", "L1", "L2", "R3", "R5", "R5", "R5",
            "L2", "L1", "R4", "R3", "R1", "L4", "L2", "L3", "R2", "L3", "L5", "L2", "L2", "L1", "L2", "R5", "L2", "L2",
            "L3", "L1", "R1", "L4", "R2", "L4", "R3", "R5", "R3", "R4", "R1", "R5", "L3", "L5", "L5", "L3", "L2", "L1",
            "R3", "L4", "R3", "R2", "L1", "R3", "R1", "L2", "R4", "L3", "L3", "L3", "L1", "L2");

    @Test
    public void turnRight() {
        assertThat(CardinalDirection.N.turn(RelativeDirection.R), is(CardinalDirection.E));
        assertThat(CardinalDirection.E.turn(RelativeDirection.R), is(CardinalDirection.S));
        assertThat(CardinalDirection.S.turn(RelativeDirection.R), is(CardinalDirection.W));
        assertThat(CardinalDirection.W.turn(RelativeDirection.R), is(CardinalDirection.N));
    }

    @Test
    public void turnLeft() {
        assertThat(CardinalDirection.N.turn(RelativeDirection.L), is(CardinalDirection.W));
        assertThat(CardinalDirection.E.turn(RelativeDirection.L), is(CardinalDirection.N));
        assertThat(CardinalDirection.S.turn(RelativeDirection.L), is(CardinalDirection.E));
        assertThat(CardinalDirection.W.turn(RelativeDirection.L), is(CardinalDirection.S));
    }

    @Test
    public void distanceBetweenPoints() {
        assertThat(new Point(0, 0).distanceFrom(new Point(0, 0)), is(0));
        assertThat(new Point(0, 0).distanceFrom(new Point(-1, 1)), is(2));
        assertThat(new Point(0, 0).distanceFrom(new Point(0, 2)), is(2));
        assertThat(new Point(0, 0).distanceFrom(new Point(-2, 0)), is(2));
    }

    @Test
    public void example1() {
        assertThat(distance(asList("R2", "L3")), is(5));
    }

    @Test
    public void example2() {
        assertThat(distance(asList("R2", "R2", "R2")), is(2));
    }

    @Test
    public void example3() {
        assertThat(distance(asList("R5", "L5", "R5", "R3")), is(12));
    }

    @Test
    public void puzzle1() {
        System.out.println("Puzzle 1:");
        System.out.println("Commands: " + PUZZLE_INPUT);
        System.out.println("Distance: " + distance(PUZZLE_INPUT) + ", expected: 279\n");
    }

    @Test
    public void example4() {
        assertThat(distanceFromFirstRevisitedPosition(asList("R8", "R4", "R4", "R8")), is(4));
    }

    @Test
    public void puzzle2() {
        System.out.println("Puzzle 2:");
        System.out.println("Commands: " + PUZZLE_INPUT);
        System.out.println("Distance: " + distanceFromFirstRevisitedPosition(PUZZLE_INPUT) + ", expected: 163");
    }
}
