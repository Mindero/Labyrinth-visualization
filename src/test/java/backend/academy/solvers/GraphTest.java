package backend.academy.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GraphTest {

    @Test
    void sourceHaveOrientedEdge() {
        Graph<Integer> g = new Graph<>();
        g.addOrientedEdge(1, 2, 5);

        Set<Graph.Edge<Integer>> expectedEdges = Set.of(new Graph.Edge<>(2, 5));
        Set<Graph.Edge<Integer>> actualEdges = g.getEdges(1);
        assertThat(actualEdges).isEqualTo(expectedEdges);
    }

    @Test
    void targetNotHaveOrientedEdge() {
        Graph<Integer> g = new Graph<>();
        g.addOrientedEdge(1, 2, 5);

        Set<Graph.Edge<Integer>> expectedEdges = Set.of();
        Set<Graph.Edge<Integer>> actualEdges = g.getEdges(2);
        assertThat(actualEdges).isEqualTo(expectedEdges);
    }

    // Добавление неориентированного ребра
    @Test
    void sourceHaveEdge() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 2, 5);

        Set<Graph.Edge<Integer>> expectedEdges = Set.of(new Graph.Edge<>(2, 5));
        Set<Graph.Edge<Integer>> actualEdges = g.getEdges(1);
        assertThat(actualEdges).isEqualTo(expectedEdges);
    }

    // Добавление неориентированного ребра
    @Test
    void targetHaveEdge() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 2, 5);

        Set<Graph.Edge<Integer>> expectedEdges = Set.of(new Graph.Edge<>(1, 5));
        Set<Graph.Edge<Integer>> actualEdges = g.getEdges(2);
        assertThat(actualEdges).isEqualTo(expectedEdges);
    }

    @Test
    void getNodeSize() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(1, 2, 5);

        assertThat(g.getNodeSize()).isEqualTo(2);
    }

    @Test
    void readMaze() {
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
        Graph<Coordinate> graph = Graph.readMaze(new Maze(2, 2, labyrinth));

        assertThat(graph.getEdges(new Coordinate(0, 0)))
            .isEqualTo(Set.of(new Graph.Edge<>(new Coordinate(1, 0), 1)));
        assertThat(graph.getEdges(new Coordinate(1, 0)))
            .isEqualTo(Set.of(new Graph.Edge<>(new Coordinate(0, 0), 1),
                    new Graph.Edge<>(new Coordinate(1, 1), 1)
                )
            );
        assertThat(graph.getEdges(new Coordinate(1, 1)))
            .isEqualTo(Set.of(new Graph.Edge<>(new Coordinate(1, 0), 1)));
        assertThat(graph.getEdges(new Coordinate(0, 1)))
            .isEqualTo(null);
    }
}
