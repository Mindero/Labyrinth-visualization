package backend.academy.generators;

import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.maze.Maze;

/**
 * Интерфейс {@code Generator} реализует генерацию лабиринта по его размерам: ширине и высоте.
 */
public interface Generator {
    /**
     * Генерирует идеальный лабиринт.
     * Лабиринт - идеальный, из каждой точки существует ровно один путь к любой другой точке.
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return идеальный лабиринт
     */
    Maze generateIdealMaze(int height, int width) throws LabyrinthSizeSmallException;

    /**
     * Генерирует не идеальный лабиринт.
     * Лабиринт не идеальный, если в нём образуются циклы.
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return неидеальный лабиринт
     */
    Maze generateNotIdealMaze(int height, int width) throws LabyrinthSizeSmallException;
}
