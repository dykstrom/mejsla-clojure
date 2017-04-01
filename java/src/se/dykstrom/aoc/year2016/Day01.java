package se.dykstrom.aoc.year2016;

import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

class Day01 {

    private static final Point START = new Point(0, 0);

    static int distance(List<String> commands) {
        CardinalDirection direction = CardinalDirection.N;
        Point position = START;

        for (String c : commands) {
            Command command = Command.from(c);
            direction = direction.turn(command.rd);
            position = position.move(command.distance, direction);
        }

        return position.distanceFrom(START);
    }

    static int distanceFromFirstRevisitedPosition(List<String> commands) {
        CardinalDirection direction = CardinalDirection.N;
        Point position = START;

        Set<Point> visitedPositions = new HashSet<>();
        visitedPositions.add(position);

        for (String c : commands) {
            Command command = Command.from(c);
            direction = direction.turn(command.rd);

            // Find the path from the current position to the next
            List<Point> path = position.path(command.distance, direction);
            // Find any position that was revisited along the path
            Optional<Point> revisitedPosition = path.stream().filter(visitedPositions::contains).findFirst();
            // If we found a revisited position
            if (revisitedPosition.isPresent()) {
                return revisitedPosition.get().distanceFrom(START);
            } else {
                visitedPositions.addAll(path);
            }

            position = position.move(command.distance, direction);
        }

        throw new IllegalStateException("No position visited twice");
    }
}

/**
 * Represents the relative directions left and right.
 */
enum RelativeDirection {

    R(1),
    L(-1);

    final int turn;

    RelativeDirection(int turn) {
        this.turn = turn;
    }
}

/**
 * Represents the cardinal directions north, east, south, and west.
 */
enum CardinalDirection {

    N(1, 0),
    E(0, 1),
    S(-1, 0),
    W(0, -1);

    final int dx;
    final int dy;

    CardinalDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Turns in the given relative direction to face in a new cardinal direction.
     */
    public CardinalDirection turn(RelativeDirection rd) {
        return values()[(ordinal() + rd.turn + 4) % 4];
    }
}

class Command {

    final RelativeDirection rd;
    final int distance;

    private Command(RelativeDirection rd, int distance) {
        this.rd = rd;
        this.distance = distance;
    }

    static Command from(String text) {
        return new Command(RelativeDirection.valueOf(text.substring(0, 1)), Integer.valueOf(text.substring(1)));
    }
}

class Point {

    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point that = (Point) o;
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Moves the given distance in the given cardinal direction.
     */
    Point move(int distance, CardinalDirection direction) {
        return new Point(x + direction.dx * distance, y + direction.dy * distance);
    }

    /**
     * Returns list of all positions visited when walking the given distance in the given cardinal direction,
     * including the end position
     */
    List<Point> path(int distance, CardinalDirection direction) {
        return IntStream.rangeClosed(1, distance).mapToObj(d -> move(d, direction)).collect(toList());
    }

    /**
     * Returns the "taxi" distance between this point and that point.
     */
    int distanceFrom(Point that) {
        return abs(this.x - that.x) + abs(this.y - that.y);
    }
}
