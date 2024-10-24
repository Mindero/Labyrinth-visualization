package backend.academy.maze;

import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MazeTest {
    private Maze maze;

    @BeforeEach
    void setMaze() {
        // Лабиринт:
        // .#
        // ..
        Cell[][] labyrinth = new Cell[2][2];
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                labyrinth[i][j] = new Cell(i, j, 1);
            }
        }
        labyrinth[0][1] = new Cell(0, 1, 0);
        maze = new Maze(2, 2, labyrinth);
    }

    @Test
    void getHeight() {
        int expectedHeight = 2;
        int actualHeight = maze.getHeight();

        assertThat(actualHeight).isEqualTo(expectedHeight);
    }

    @Test
    void getWidth() {
        int expectedWidth = 2;
        int actualWidth = maze.getWidth();

        assertThat(actualWidth).isEqualTo(expectedWidth);
    }

    @Test
    void getMaze() {
        Cell[][] expectedLabyrinth = new Cell[2][2];
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                expectedLabyrinth[i][j] = new Cell(i, j, 1);
            }
        }
        expectedLabyrinth[0][1] = new Cell(0, 1, 0);

        Cell[][] actualLabyrinth = maze.getMaze();

        assertThat(actualLabyrinth).isEqualTo(expectedLabyrinth);
    }

    @Test
    void getCell() {
        Cell expectedCell = new Cell(0, 1, 0);
        Cell actualCell = maze.getCell(0, 1);

        assertThat(actualCell).isEqualTo(expectedCell);

        actualCell = maze.getCell(new Coordinate(0, 1));

        assertThat(actualCell).isEqualTo(expectedCell);
    }

    @Test
    void getLeftTopPassageCell() {
        Coordinate expectedLetTopPassageCell = new Coordinate(0, 0);
        Coordinate actualLetTopPassageCell = maze.getLeftTopPassageCell();

        assertThat(actualLetTopPassageCell).isEqualTo(expectedLetTopPassageCell);
    }

    @Test
    void getRightBottomPassageCell() {
        Coordinate expectedRightBottomPassageCell = new Coordinate(1, 1);
        Coordinate actualRightBottomPassageCell = maze.getRightBottomPassageCell();

        assertThat(actualRightBottomPassageCell).isEqualTo(expectedRightBottomPassageCell);
    }

    @Test
    void cellsIsWall() {
        boolean isWall = maze.cellIsWall(new Coordinate(0, 1));

        assertThat(isWall).isTrue();

        isWall = maze.cellIsWall(0, 1);

        assertThat(isWall).isTrue();
    }

    @Test
    void cellsIsNotWall() {
        boolean isWall = maze.cellIsWall(new Coordinate(0, 0));

        assertThat(isWall).isFalse();

        isWall = maze.cellIsWall(0, 0);

        assertThat(isWall).isFalse();
    }
}
