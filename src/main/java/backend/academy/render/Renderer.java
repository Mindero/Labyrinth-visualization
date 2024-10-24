package backend.academy.render;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import java.util.List;

/**
 * {@code Renderer} реализует перевод лабиринта и кратчайшего пути
 * в человеко-читабельную строку.
 */
public interface Renderer {
    // Перевод лабиринта в строку.
    String render(Maze maze);

    // Перевод лабиринта вместе с кратчайшим путём.
    String render(Maze maze, List<Coordinate> path);

    // Вывод лабиринта в каждой момент работы алгоритма
    List<String> solverRender(Maze maze, List<Coordinate> solverSequence);
}
