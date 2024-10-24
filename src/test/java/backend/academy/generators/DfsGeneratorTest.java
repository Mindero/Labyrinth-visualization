package backend.academy.generators;

import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import backend.academy.solvers.Graph;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.junit.jupiter.api.RepeatedTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DfsGeneratorTest {
    private final DfsGenerator dfsGenerator = new DfsGenerator();

    @RepeatedTest(100)
    void idealMazeIsConnected() throws LabyrinthSizeSmallException {
        Maze maze = dfsGenerator.generateIdealMaze(10, 10);
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

    @RepeatedTest(100)
    void notIdealMazeIsConnected() throws LabyrinthSizeSmallException {
        Maze maze = dfsGenerator.generateNotIdealMaze(10, 10);
        Graph<Coordinate> graph = Graph.readMaze(maze);
        // BFS
        Queue<Coordinate> queue = new ArrayDeque<>();
        Set<Coordinate> actualUsed = new HashSet<>();
        queue.add(maze.getLeftTopPassageCell());
        actualUsed.add(maze.getLeftTopPassageCell());
        while (!queue.isEmpty()) {
            Coordinate from = queue.poll();
            for (Graph.Edge<Coordinate> edge : graph.getEdges(from)) {
                Coordinate to = edge.to();
                if (!actualUsed.contains(to)) {
                    actualUsed.add(to);
                    queue.add(to);
                }
            }
        }

        Set<Coordinate> expectedUsed = new HashSet<>();
        for (int row = 0; row < 10; ++row) {
            for (int col = 0; col < 10; ++col) {
                if (!maze.cellIsWall(row, col)) {
                    expectedUsed.add(new Coordinate(row, col));
                }
            }
        }

        assertThat(actualUsed).isEqualTo(expectedUsed);
    }

    // Лабиринт не содержит циклов
    @RepeatedTest(100)
    void mazeIsIdeal() throws LabyrinthSizeSmallException {
        Maze maze = dfsGenerator.generateIdealMaze(10, 10);
        Graph<Coordinate> graph = Graph.readMaze(maze);
        // BFS
        Queue<Coordinate> queue = new ArrayDeque<>();
        Set<Coordinate> used = new HashSet<>();
        Map<Coordinate, Coordinate> pr = new HashMap<>(); // Предки
        queue.add(maze.getLeftTopPassageCell());
        used.add(maze.getLeftTopPassageCell());
        pr.put(maze.getLeftTopPassageCell(), maze.getLeftTopPassageCell());
        while (!queue.isEmpty()) {
            Coordinate from = queue.poll();
            for (Graph.Edge<Coordinate> edge : graph.getEdges(from)) {
                Coordinate to = edge.to();
                if (!used.contains(to)) {
                    used.add(to);
                    pr.put(to, from);
                    queue.add(to);
                }
                // Если ранее посещали эту вершину, то значит мы должны были из неё прийти
                else {
                    assertThat(pr.get(from)).isEqualTo(to);
                }
            }
        }
    }
}
