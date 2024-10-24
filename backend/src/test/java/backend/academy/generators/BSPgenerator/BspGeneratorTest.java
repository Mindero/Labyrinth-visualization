package backend.academy.generators.BSPgenerator;

import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import backend.academy.solvers.Graph;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import org.junit.jupiter.api.RepeatedTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BspGeneratorTest {

    private final BspGenerator bspGenerator = new BspGenerator();

    @RepeatedTest(100)
    void mazeIsConnected() throws LabyrinthSizeSmallException {
        Maze maze = bspGenerator.generateNotIdealMaze(10, 10);
        Graph<Coordinate> graph = Graph.readMaze(maze);
        // BFS
        Queue<Coordinate> queue = new ArrayDeque<>();
        Set<Coordinate> used = new HashSet<>();
        queue.add(maze.getLeftTopPassageCell());
        used.add(maze.getLeftTopPassageCell());
        while (!queue.isEmpty()) {
            Coordinate from = queue.poll();
            for (Graph.Edge<Coordinate> edge : graph.getEdges(from)) {
                Coordinate to = edge.to();
                if (!used.contains(to)) {
                    used.add(to);
                    queue.add(to);
                }
            }
        }

        // Проверяем, что все вершины были посещены
        for (int row = 0; row < 10; ++row) {
            for (int col = 0; col < 10; ++col) {
                if (!maze.cellIsWall(row, col)) {
                    assertThat(used.contains(new Coordinate(row, col))).isTrue();
                }
            }
        }
    }
}
