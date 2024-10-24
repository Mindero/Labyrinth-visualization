package backend.academy.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Находит кратчайший путь в лабиринте с помощью алгоритма Дейкстры
 */
public class DijkstraSolver implements Solver {

    private List<Coordinate> algoSequence; // Последовательность точек, которые рассматривает алгоритм

    public DijkstraSolver() {
    }

    public List<Coordinate> findPath(Graph<Coordinate> graph, Coordinate start, Coordinate end) {
        // Обнулим историю работы алгоритма
        algoSequence = new ArrayList<>();
        // Массив расстояний
        Map<Coordinate, Integer> dist = new HashMap<>();
        // Массив предков
        Map<Coordinate, Coordinate> pr = new HashMap<>();

        int nodeSize = graph.getNodeSize();
        // Множество, рассматриваемых вершин
        Set<Coordinate> waitingNode = new HashSet<>();
        // Изначальные значения для стартовой вершины
        dist.put(start, 0);
        pr.put(start, start);
        waitingNode.add(start);
        while (nodeSize > 0) {
            // Получаем вершину с минимальным расстоянием
            Coordinate vertex = getLowerWaitingNode(waitingNode, dist);
            algoSequence.add(vertex);
            waitingNode.remove(vertex);
            // Если дошли до конечной вершины, то выводим путь
            if (vertex.equals(end)) {
                return RecoverPathUtility.recoverPath(start, end, pr);
            }
            --nodeSize;
            int vertexDist = dist.get(vertex);
            for (Graph.Edge<Coordinate> edge : graph.getEdges(vertex)) {
                Coordinate to = edge.to();
                int weight = edge.weight();
                // Пытаемся обновить расстояние до вершины
                if (!dist.containsKey(to) || dist.get(to) > vertexDist + weight) {
                    // Если получается, то обновляем расстояние и наши рассматриваемые вершины
                    dist.put(to, vertexDist + weight);
                    waitingNode.add(to);
                    pr.put(to, vertex);
                }
            }
        }
        return RecoverPathUtility.recoverPath(start, end, pr);
    }

    @Override
    public List<Coordinate> findPath(Maze maze, Coordinate start, Coordinate end) {
        // Если пути точно не существует
        if (maze.cellIsWall(start) || maze.cellIsWall(end)) {
            return new ArrayList<>();
        }
        // Составляем граф по лабиринту
        Graph<Coordinate> graph = Graph.readMaze(maze);
        // Находим путь в графе
        return findPath(graph, start, end);
    }

    // Получение вершины с минимальным расстоянием
    private Coordinate getLowerWaitingNode(Set<Coordinate> nodes, Map<Coordinate, Integer> dist) {
        Integer minDist = null;
        Coordinate lowerNode = null;
        for (Coordinate node : nodes) {
            Integer nodeDist = dist.get(node);
            if (minDist == null || minDist > nodeDist) {
                minDist = nodeDist;
                lowerNode = node;
            }
        }
        return lowerNode;
    }

    @Override
    public List<Coordinate> algorithmSimulate(Maze maze, Coordinate start, Coordinate end) {
        findPath(maze, start, end);
        return algoSequence;
    }
}
