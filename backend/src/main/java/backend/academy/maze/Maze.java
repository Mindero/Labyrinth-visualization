package backend.academy.maze;

import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import java.util.List;

public class Maze {
    private final int height;
    private final int width;
    private final Cell[][] maze;

    public Maze(int height, int width, Cell[][] maze) {
        this.height = height;
        this.width = width;
        this.maze = maze;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell[][] getMaze() {
        return maze;
    }

    public Cell getCell(int row, int col) {
        return maze[row][col];
    }

    public boolean cellIsWall(int row, int col) {
        // Если клетка вышла за границы
        if (row < 0 || row >= height || col < 0 || col >= width) {
            return true;
        }
        return getCell(row, col).isWall();
    }

    public int getCellWeight(int row, int col) {
        return getCell(row, col).getWeight();
    }

    public Cell getCell(Coordinate coordinate) {
        return getCell(coordinate.row(), coordinate.column());
    }

    public boolean cellIsWall(Coordinate coordinate) {
        return cellIsWall(coordinate.row(), coordinate.column());
    }

    public int getCellWeight(Coordinate coordinate) {
        return getCellWeight(coordinate.row(), coordinate.column());
    }

    // Получить левую верхнюю клетку, не являющеюся стеной
    public Coordinate getLeftTopPassageCell() {
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (!maze[i][j].isWall()) {
                    return new Coordinate(i, j);
                }
            }
        }
        throw new RuntimeException();
    }

    // Получить правую нижнюю клетку, не являющеюся стеной
    public Coordinate getRightBottomPassageCell() {
        for (int i = height - 1; i >= 0; --i) {
            for (int j = width - 1; j >= 0; --j) {
                if (!maze[i][j].isWall()) {
                    return new Coordinate(i, j);
                }
            }
        }
        throw new RuntimeException();
    }

    public Maze addObstacles (List<Coordinate> obstacles){
        Cell[][] newMaze = maze.clone();
        obstacles.forEach(coordinate -> {
            newMaze[coordinate.column()][coordinate.row()] =
                new Cell(coordinate, Cell.Type.OBSTACLE);
        });
        return new Maze(height, width, newMaze);
    }
}
