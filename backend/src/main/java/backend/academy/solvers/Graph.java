package backend.academy.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import backend.academy.maze.cell.Direction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph<T> {
    // Список смежности
    private final Map<T, Set<Edge<T>>> adj;

    public Graph() {
        adj = new HashMap<>();
    }

    // Построение графа по лабиринту
    public static Graph<Coordinate> readMaze(Maze maze) {
        Graph<Coordinate> graph = new Graph<>();
        int width = maze.getWidth();
        int height = maze.getHeight();
        Direction[] directions = Direction.values();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // Если клетка стена, то пропускаем
                if (maze.cellIsWall(i, j)) {
                    continue;
                }
                Coordinate cur = new Coordinate(i, j);
                // Смотрим по всем сторонам от клетки
                for (var direction : directions) {
                    Coordinate next = new Coordinate(cur, direction);
                    if (maze.cellIsWall(next)) {
                        continue;
                    }
                    // Если клетка рядом является проходом, то добавим ребро
                    graph.addEdge(cur, next, maze.getCellWeight(next));
                }
            }
        }
        return graph;
    }

    private void addVertex(T node) {
        // Если вершина уже есть
        if (adj.containsKey(node)) {
            return;
        }
        adj.put(node, new HashSet<>());
    }

    // Добавление ориентированного ребра в граф
    public void addOrientedEdge(T from, T to, int weight) {
        // Сначала добавляем вершины, если их нет
        addVertex(from);
        addVertex(to);

        var edges = adj.get(from);
        edges.add(new Edge<T>(to, weight));
        adj.put(from, edges);
    }

    // Добавление неориентированного ребра в граф
    public void addEdge(T from, T to, int weight) {
        addOrientedEdge(from, to, weight);
        addOrientedEdge(to, from, weight);
    }

    // Получить список всех ребер, исходящих из вершины
    public Set<Edge<T>> getEdges(T from) {
        return adj.get(from);
    }

    public int getNodeSize() {
        return adj.size();
    }

    public record Edge<T>(T to, int weight) {
    }
}
