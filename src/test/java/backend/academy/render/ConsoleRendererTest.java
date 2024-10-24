package backend.academy.render;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ConsoleRendererTest {

    private final ConsoleRenderer consoleRenderer = new ConsoleRenderer();
    private Maze maze;

    @BeforeEach
    void setUp() {
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
    void mazeRender() {
        String expectedRender = "----\n| #|\n|  |\n----\n";
        String actualRender = consoleRenderer.render(maze);

        assertThat(actualRender).isEqualTo(expectedRender);
    }

    @Test
    void pathRender() {
        String expectedRender = "----\n|x#|\n|xx|\n----\n";
        String actualRender = consoleRenderer.render(maze, List.of(new Coordinate(0, 0)
            , new Coordinate(1, 0), new Coordinate(1, 1)));

        assertThat(actualRender).isEqualTo(expectedRender);
    }

    @Test
    void solverRender() {
        List<String> expectedRender = List.of("----\n|x#|\n|  |\n----\n",
            "----\n|x#|\n|x |\n----\n",
            "----\n|x#|\n|xx|\n----\n");

        List<String> actualRender = consoleRenderer.solverRender(maze, List.of(new Coordinate(0, 0)
            , new Coordinate(1, 0), new Coordinate(1, 1)));

        assertThat(actualRender).isEqualTo(expectedRender);
    }
}
