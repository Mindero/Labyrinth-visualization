package backend.academy.maze.cell;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinate(@JsonProperty int row, @JsonProperty int column) {
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
