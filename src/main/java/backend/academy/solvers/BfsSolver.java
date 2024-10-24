package backend.academy.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Находит кратчайший путь между стартовой и конечной вершиной
 * алгоритмом обхода в ширину.
 * Для взвешенного графа работает неэффективно.
 */
public class BfsSolver implements Solver {
    // Последовательность точек, рассматирваемых в процессе работы алгоритма
    private List<Coordinate> algoSequence;

    public BfsSolver() {

    }

    public List<Coordinate> findPath(Graph<Coordinate> g, Coordinate start, Coordinate end) {
        algoSequence = new ArrayList<>();
        Map<Coordinate, Integer> dist = new HashMap<>();
        // Сохраняем предков
        Map<Coordinate, Coordinate> pr = new HashMap<>();

        Queue<Coordinate> queue = new ArrayDeque<>();
        dist.put(start, 0);
        pr.put(start, start);
        queue.add(start);
        // Пока не прошли все вершины
        while (!queue.isEmpty()) {
            // Извлекаем голову и удаляем её
            Coordinate head = queue.poll();
            algoSequence.add(head);
            // Если дошли до конечной точки, то заканчиваем алгоритм
            if (head.equals(end)) {
                break;
            }
            Integer distHead = dist.get(head);
            // Пройдёмся по всем ребрам из вершины
            for (Graph.Edge<Coordinate> edge : g.getEdges(head)) {
                Coordinate to = edge.to();
                // Если можем обновить расстояние до to
                if (!dist.containsKey(to) || dist.get(to) > distHead + edge.weight()) {
                    // Обновляем расстояние до to и ставим откуда пришли в to.
                    dist.put(to, distHead + edge.weight());
                    pr.put(to, head);
                    // Добавляем эту вершину в очередь
                    queue.add(to);
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
        Graph<Coordinate> g = Graph.readMaze(maze);
        return findPath(g, start, end);
    }

    // Получения последовательности точек, которые рассматривает алгоритм
    @Override
    public List<Coordinate> algorithmSimulate(Maze maze, Coordinate start, Coordinate end) {
        findPath(maze, start, end);
        return algoSequence;
    }
}
