package backend.academy.generators;

import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import backend.academy.maze.cell.Direction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * {@code DfsGenerator} создает лабиринт методом обхода в глубину (DFS).
 * Созданный лабиринт является идеальным, то любая пара вершин
 * в лабиринте соединена ровно одним путём.
 */
public class DfsGenerator implements Generator {
    // Список направлений обхода в лабиринте
    private static final List<Direction> DIRECTIONS = Arrays.asList(Direction.values());
    // Процент стен, которые останется, после превращения идеального лабиринта в неидеальный.
    private static final double PERCENTAGE_OF_SAVED_WALLS = 0.9;
    // Ширина лабиринта
    private int width;
    // Высота лабиринта
    private int height;
    /*
     * Массив просмотренных вершин в обходе в глубину
     * used[i][j] = 0, если клетка является стеной
     * used[i][j] = 1, если клетка является проходом и она обрабатывалась в dfs
     */
    private int[][] used;

    public DfsGenerator() {
    }

    /**
     * Создание идеального лабиринта через обход в глубину
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return идеальный лабиринт
     */
    @Override
    @SuppressWarnings("MultipleStringLiterals")
    public Maze generateIdealMaze(int height, int width) throws LabyrinthSizeSmallException {
        if (height <= 0 || width <= 0) {
            throw new LabyrinthSizeSmallException("Размер лабиринта должен быть неотрицательных числом");
        }
        this.height = height;
        this.width = width;
        /*
            used[i][j] = 1, если в эту вершину dfs уже заходил
            во всех остальных случаях used[i][j] = 0
         */
        used = new int[height][width];
        dfs(new Coordinate(0, 0));
        return createMaze();
    }

    /**
     * Неидеального лабиринт генерируется путём создания идеального
     * и последовательного удаления нескольких стен, тем самым создавая циклы.
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return неидеальный лабиринт
     */
    @Override
    public Maze generateNotIdealMaze(int height, int width) throws LabyrinthSizeSmallException {
        if (height <= 0 || width <= 0) {
            throw new LabyrinthSizeSmallException("Размер лабиринта должен быть неотрицательных числом");
        }
        // Получаем идеальный лабиринт
        Maze idealMaze = generateIdealMaze(height, width);
        // Получаем все координаты стен лабиринта
        List<Coordinate> wallsCoordinate = new ArrayList<>();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (idealMaze.cellIsWall(i, j)) {
                    wallsCoordinate.add(new Coordinate(i, j));
                }
            }
        }

        Cell[][] notIdealMaze = idealMaze.getMaze();
        // Удаляем максимум половину всех стен
        int initialSizeWalls = wallsCoordinate.size();
        int expectedSizeWalls = (int) (wallsCoordinate.size() * PERCENTAGE_OF_SAVED_WALLS);
        for (int i = initialSizeWalls; i > expectedSizeWalls; --i) {
            // Получаем случайную клетку
            Coordinate deletedWall = wallsCoordinate.get(ThreadLocalRandom
                .current().nextInt(initialSizeWalls));
            // Проверяем можно ли удалить стену и если да, то удаляем
            if (canDeleteWall(deletedWall, notIdealMaze)) {
                notIdealMaze[deletedWall.row()][deletedWall.column()] =
                    new Cell(deletedWall.row(), deletedWall.column(), 1);
            }
        }
        return new Maze(height, width, notIdealMaze);
    }

    /**
     * Функция проверяет можно ли клетку превратить в проход, не сделав при этом новую компоненту связности.
     * Превратить в проход можно, если она соединена хотя с одним проходом
     *
     * @param deletedWall координаты клетки, которую хотим сделать проходом
     * @param labyrinth   лабиринт
     * @return true, если клетку можно превратить в проход
     *     false иначе
     */
    private boolean canDeleteWall(Coordinate deletedWall, Cell[][] labyrinth) {
        // Проходимся по соседям клетки
        for (var dir : DIRECTIONS) {
            Coordinate next = new Coordinate(deletedWall, dir);
            // Если соседняя клетка не стена, значит можем превратить в проход
            if (validCell(next) && !labyrinth[next.row()][next.column()].isWall()) {
                return true;
            }
        }
        // Нельзя, так как создастся изолированная клетка
        return false;
    }

    private void dfs(Coordinate cur) {
        // Помечаем, что заходили в эту клетку.
        // То есть клетка является проходом в лабиринте
        used[cur.row()][cur.column()] = 1;
        // Перетасовываем направление движения в обходе
        Collections.shuffle(DIRECTIONS);
        for (var dir : DIRECTIONS) {
            // Получаем клетку по направлению движения
            Coordinate newCur = new Coordinate(cur, dir);
            /*
             * Если клетка выходит за пределы поля
             * ИЛИ если, зайдя в эту вершину, создаётся цикл,
             * ТО пропускаем её
             */
            if (!validCell(newCur) || cycleCreated(newCur)) {
                continue;
            }
            // Если уже были в этой клетке, также её пропускаем
            if (used[newCur.row()][newCur.column()] == 1) {
                continue;
            }
            // Иначе всё хорошо и рекурсивно вызываем обход от новой вершины
            dfs(newCur);
        }
    }

    /**
     * Создает объект Maze (лабиринт) по результату обхода dfs.
     * Результат обхода dfs лежит в массиве used:
     * used[i][j] = 1 => проход
     * used[i][j] = 0 => стена
     */
    private Maze createMaze() {
        Cell[][] maze = new Cell[height][width];
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                maze[row][col] = new Cell(row, col, used[row][col]);
            }
        }
        return new Maze(height, width, maze);
    }

    /**
     * Проверяет клетку, что она не вышла за границы лабиринта
     *
     * @param coordinate координата в лабиринте
     * @return true, если координаты выходят за границу лабиринта
     *     false, если НЕ выходит за границу лабиринта
     */
    private boolean validCell(Coordinate coordinate) {
        return coordinate.row() >= 0 && coordinate.row() < height
            && coordinate.column() >= 0 && coordinate.column() < width;
    }

    /**
     * Функция проверяет, создастся ли цикл, если сделать
     * клетку с координатами coordinate проходом.
     * <p>
     * Рассмотрим клетки слева, справа, сверху и снизу от coordinate.
     * Посчитаем сколько из них являются проходами.
     * <p>
     * Цикл создастся, если таким клеток >= 2.
     * <p>
     * Это так, потому что DFS по сути создает дерево обхода.
     * Каждая "помеченная вершина" является вершиной в этом дереве.
     * Соответственно, если в окрестности coordinate есть хотя бы две вершины из этого дерева,
     * то соединив их, мы получим цикл в этом дереве.
     *
     * @param coordinate координата в лабиринте
     * @return true - если, сделав coordinate проходом, создастся цикл
     *     false - иначе
     */
    private boolean cycleCreated(Coordinate coordinate) {
        int count = 0; // Счётчик = кол-во проходов в окрестности coordinate
        for (var dir : DIRECTIONS) {
            Coordinate newCur = new Coordinate(coordinate, dir);
            // Если клетка в окрестности не выходит за пределы лабиринта
            // И является помеченной dfs, то прибавим к счётчику
            if (validCell(newCur) && used[newCur.row()][newCur.column()] > 0) {
                ++count;
            }
        }
        return count >= 2;
    }
}
