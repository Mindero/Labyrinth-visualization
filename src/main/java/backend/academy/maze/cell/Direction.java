package backend.academy.maze.cell;

public enum Direction {
    UP(new Coordinate(-1, 0)),
    BOT(new Coordinate(1, 0)),
    LEFT(new Coordinate(0, -1)),
    RIGHT(new Coordinate(0, 1));

    private final Coordinate coordinate;

    Direction(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
