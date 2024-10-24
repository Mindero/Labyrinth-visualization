package backend.academy.render;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ColorRendererTest {
    private final ColorRenderer colorRenderer = new ColorRenderer();
    private Maze maze;
    private String[][] expectedRenderedMaze;

    @BeforeEach
    void setUp() {
        // Лабиринт:
        // .#
        // ..
        Cell[][] labyrinth = new Cell[2][2];
        expectedRenderedMaze = new String[2][2];
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                labyrinth[i][j] = new Cell(i, j, 1);
                expectedRenderedMaze[i][j] = setColor(ColorRenderer.ANSI_PASSAGE);
            }
        }
        labyrinth[0][1] = new Cell(0, 1, 0);
        expectedRenderedMaze[0][1] = setColor(ColorRenderer.ANSI_WALL);
        maze = new Maze(2, 2, labyrinth);
    }

    private String setColor(String ANSI_color, Character character) {
        return ANSI_color + character + ColorRenderer.ANSI_RESET;
    }

    private String setColor(String ANSI_color) {
        return setColor(ANSI_color, ' ');
    }

    private String renderedMazeToString(String[][] array) {
        StringBuilder sb = new StringBuilder();
        sb.append(setColor(ColorRenderer.ANSI_WALL, '-').repeat(array[0].length + 2));
        sb.append("\n");
        for (String[] row : array) {
            sb.append(setColor(ColorRenderer.ANSI_WALL, '|'));
            for (String element : row) {
                sb.append(element);
            }
            sb.append(setColor(ColorRenderer.ANSI_WALL, '|'));
            sb.append("\n");
        }
        sb.append(setColor(ColorRenderer.ANSI_WALL, '-').repeat(array[0].length + 2));
        sb.append("\n");
        return sb.toString();
    }

    @Test
    void mazeRender() {
        String expectedRender = renderedMazeToString(expectedRenderedMaze);
        String actualRender = colorRenderer.render(maze);

        assertThat(actualRender).isEqualTo(expectedRender);
    }

    @Test
    void pathRender() {
        String[][] expectedRenderedMazeWithPath = Arrays.copyOf(expectedRenderedMaze, expectedRenderedMaze.length);
        expectedRenderedMazeWithPath[0][0] = setColor(ColorRenderer.ANSI_START_CELL);
        expectedRenderedMazeWithPath[1][0] = setColor(ColorRenderer.ANSI_PATH);
        expectedRenderedMazeWithPath[1][1] = setColor(ColorRenderer.ANSI_END_CELL);

        String expectedRender = renderedMazeToString(expectedRenderedMazeWithPath);
        String actualRender = colorRenderer.render(maze, List.of(new Coordinate(0, 0)
            , new Coordinate(1, 0), new Coordinate(1, 1)));

        assertThat(actualRender).isEqualTo(expectedRender);
    }

    @Test
    void solverRender() {
        String[][] expectedRenderedMazeWithPath = Arrays.copyOf(expectedRenderedMaze, expectedRenderedMaze.length);
        List<String> expectedRender = new ArrayList<>();

        expectedRenderedMazeWithPath[0][0] = setColor(ColorRenderer.ANSI_PATH);
        expectedRender.add(renderedMazeToString(expectedRenderedMazeWithPath));
        expectedRenderedMazeWithPath[1][0] = setColor(ColorRenderer.ANSI_PATH);
        expectedRender.add(renderedMazeToString(expectedRenderedMazeWithPath));
        expectedRenderedMazeWithPath[1][1] = setColor(ColorRenderer.ANSI_PATH);
        expectedRender.add(renderedMazeToString(expectedRenderedMazeWithPath));

        List<String> actualRender = colorRenderer.solverRender(maze, List.of(new Coordinate(0, 0)
            , new Coordinate(1, 0), new Coordinate(1, 1)));

        assertThat(actualRender).isEqualTo(expectedRender);
    }
}
