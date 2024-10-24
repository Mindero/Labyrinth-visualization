package backend.academy.maze.cell;

public record Coordinate(int row, int column) {
    public Coordinate(Coordinate coordinate, Direction dir) {
        this(coordinate.row + dir.getCoordinate().row,
            coordinate.column() + dir.getCoordinate().column);
    }

    public static Coordinate copyOf(Coordinate cord) {
        return new Coordinate(cord.row, cord.column);
    }

    public int getX() {
        return column;
    }

    public int getY() {
        return row;
    }
}
