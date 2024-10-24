package backend.academy.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import java.util.List;

/**
 * {@code Solver} реализует алгоритм поиска кратчайший путь
 * из стартовой точки в конечную
 */
public interface Solver {
    // Нахождение пути из стартовой точки в конечную
    List<Coordinate> findPath(Maze maze, Coordinate start, Coordinate end);

    // Список точек, которые просматриваются в алгоритме
    // при поиске кратчайшего пути из стартовой в конечную точки
    List<Coordinate> algorithmSimulate(Maze maze, Coordinate start, Coordinate end);
}
