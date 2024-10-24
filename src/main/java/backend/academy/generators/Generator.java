package backend.academy.generators;

import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.maze.Maze;

/**
 * Интерфейс {@code Generator} реализует генерацию лабиринта по его размерам: ширине и высоте.
 */
public interface Generator {
    /**
     * Генерирует идеальный лабиринт
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return идеальный лабиринт
     */
    Maze generateIdealMaze(int height, int width) throws LabyrinthSizeSmallException;

    /**
     * Генерирует не идеальный лабиринт
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return неидеальный лабиринт
     */
    Maze generateNotIdealMaze(int height, int width) throws LabyrinthSizeSmallException;
}
