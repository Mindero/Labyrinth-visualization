package backend.academy.maze.cell;

public record Cell(Coordinate coordinate, Type cellType) {
    public Cell(int row, int col, int isPassage) {
        this(new Coordinate(row, col), (isPassage == 1) ? Type.PASSAGE : Type.WALL);
    }
    public Cell(int row, int col, boolean isPassage) {
        this(new Coordinate(row, col), (isPassage) ? Type.PASSAGE : Type.WALL);
    }
    public int row() {
        return coordinate.row();
    }

    public int column() {
        return coordinate.column();
    }

    public Cell setPassage() {
        return new Cell(coordinate, Type.PASSAGE);
    }

    public boolean isWall() {
        return cellType.equals(Type.WALL);
    }

    public int getWeight() {
        // TODO: сделать разный вес под разный тип клетки
        return 1;
    }

    public enum Type {
        WALL,
        PASSAGE
    }
}
