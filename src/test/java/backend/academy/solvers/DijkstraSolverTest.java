package backend.academy.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DijkstraSolverTest {

    private final DijkstraSolver dijkstraSolver = new DijkstraSolver();

    private static Graph<Coordinate> getWeightedCycleGraph() {
        Graph<Coordinate> g = new Graph<>();
        /*
            Ребра:
            (0, 0) <-> (0, 1), weight = 1
            (0, 0) <-> (1, 0), weight = 7
            (0, 1) <-> (1, 1), weight = 2
            (1, 0) <-> (1, 1), weight = 3
            (1, 0) <-> (2, 0), weight = 3
         */
        g.addEdge(new Coordinate(0, 0), new Coordinate(0, 1), 1);
        g.addEdge(new Coordinate(0, 0), new Coordinate(1, 0), 7);
        g.addEdge(new Coordinate(0, 1), new Coordinate(1, 1), 2);
        g.addEdge(new Coordinate(1, 0), new Coordinate(1, 1), 3);
        g.addEdge(new Coordinate(1, 0), new Coordinate(2, 0), 3);
        return g;
    }

    @Test
    void findPathFromGraph() {
        Graph<Coordinate> g = getWeightedCycleGraph();

        List<Coordinate> expectedPath = List.of(new Coordinate(0, 0), new Coordinate(0, 1),
            new Coordinate(1, 1), new Coordinate(1, 0), new Coordinate(2, 0));
        List<Coordinate> actualPath = dijkstraSolver.findPath(g,
            new Coordinate(0, 0),
            new Coordinate(2, 0));
        assertThat(actualPath).isEqualTo(expectedPath);
    }

    @Test
    void findPathFromSimpleMaze() {
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
        Maze maze = new Maze(2, 2, labyrinth);

        List<Coordinate> expectedPath = List.of(new Coordinate(0, 0), new Coordinate(1, 0),
            new Coordinate(1, 1));
        List<Coordinate> actualPath = dijkstraSolver.findPath(maze,
            new Coordinate(0, 0),
            new Coordinate(1, 1));

        assertThat(actualPath).isEqualTo(expectedPath);
    }
}
