package backend.academy.generators.BSPgenerator;

import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.generators.Generator;
import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Генерирует лабиринт на основе BSP дерева.
 * BSP дерево реализовано в классе {@code BspTree}.
 * Лабиринт, получающийся таким образом, похож на уровень в какой-нибудь 2D игре.
 * <p>
 * Главное отличие такого лабиринта в том, что объектами являются
 * комнаты, которые соединены с другими комнатами.
 * Проходы между комнатами МОГУТ образовывать цикл.
 */
public class BspGenerator implements Generator {
    private static final int CHANCE_TO_SUCCESSFUL_SPLIT = 10;

    public BspGenerator() {

    }

    /**
     * Создает лабиринт, где каждая клетка является стеной.
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return возвращает лабиринт без проходов, то есть каждая клетка является стеной.
     */
    private static Cell[][] createEmptyMaze(int height, int width) {
        Cell[][] mazeCells = new Cell[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                // Считаем каждую клетку за стену
                mazeCells[i][j] = new Cell(i, j, 0);
            }
        }
        return mazeCells;
    }

    /**
     * Создает неидеальный лабиринт
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return неидеальный лабиринт с помощью BSP дерева
     * @throws LabyrinthSizeSmallException если размер лабиринта слишком мал для создания больше 1 комнаты
     */
    @Override
    public Maze generateNotIdealMaze(int height, int width) throws LabyrinthSizeSmallException {
        if (height <= 2 * BspTree.MIN_SIZE || width <= BspTree.MIN_SIZE) {
            throw new LabyrinthSizeSmallException("Размер лабиринта должен быть хотя бы " + 2 * BspTree.MIN_SIZE);
        }
        // Создаем BSP дерево. Получаем корень BSP дерева
        BspTree root = generateBspTree(height, width);
        // Создаем комнаты
        root.createRooms();
        // Формируем лабиринт
        // Считаем изначально каждую клетку за стену
        Cell[][] mazeCells = createEmptyMaze(height, width);
        // Узнаем из дерева какие клетки являются проходами и записываем их
        fillTree(mazeCells, root);
        return new Maze(height, width, mazeCells);
    }

    // Не поддерживается, так как BSP дерево может создать лабиринт, содержащий циклы.
    @Override
    public Maze generateIdealMaze(int height, int width) {
        throw new UnsupportedOperationException("Генерация лабиринта BSP деревом не может создать идеальный лабиринт");
    }

    /**
     * Формирует лабиринт из BSP дерева
     *
     * @param mazeCells лабиринт
     * @param root      корень BSP дерева
     */
    private void fillTree(Cell[][] mazeCells, BspTree root) {
        // Если вершины не существует
        if (root == null) {
            return;
        }
        // Получаем комнату, которой возможно нет в этой вершине
        Rectangle room = root.getRoom();
        if (room != null) {
            // Если комната есть, то отрисовываем её
            fillRoom(mazeCells, room);
        }
        // Отрисовываем все проходы между комнатами
        for (Rectangle hall : root.getHalls()) {
            fillRoom(mazeCells, hall);
        }
        // Рекурсивно формируем лабиринт от левого и правого сына корня
        fillTree(mazeCells, root.getLeftChild());
        fillTree(mazeCells, root.getRightChild());
    }

    /**
     * Отрисовываем прямоугольник в лабиринт
     *
     * @param mazeCells лабиринт
     * @param rectangle прямоугольник с целочисленными координатами и целочисленными размерами,
     *                  который является в лабиринте проходом.
     */
    private void fillRoom(Cell[][] mazeCells, Rectangle rectangle) {
        // Проходим по всем клеткам прямоугольника и говорим,
        // что эти клетки НЕ являются больше стенами
        for (int j = rectangle.x; j < rectangle.x + rectangle.width; ++j) {
            for (int i = rectangle.y; i < rectangle.y + rectangle.height; ++i) {
                mazeCells[i][j] = mazeCells[i][j].setPassage();
            }
        }
    }

    /**
     * Создание BSP дерева на основе размеров лабиринта
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return возвращает корень BSP дерева
     */
    private BspTree generateBspTree(int height, int width) {
        // Список всех вершин BSP дерева
        List<BspTree> tree = new ArrayList<>();
        // Создаем корень дерева, изначально содержащий весь размер лабиринта
        BspTree root = new BspTree(0, 0, width, height);
        tree.add(root);
        // Если в какой-то итерации ни разу не удалось разбить область на две,
        // то прекращаем работу алгоритма.
        boolean splitWasDone = true;
        while (splitWasDone) {
            splitWasDone = false;
            // Проходимся по всем вершинам дерева
            for (int i = 0; i < tree.size(); ++i) {
                BspTree node = tree.get(i);
                // Если область ещё не была разделена
                if (node.getLeftChild() == null && node.getRightChild() == null) {
                    // Если удалось успешно разделить область
                    if (successfulSplit(node)) {
                        // Область разбилась на две, добавляем левую и правую часть
                        tree.add(node.getLeftChild());
                        tree.add(node.getRightChild());
                        splitWasDone = true;
                    }
                }
            }
        }
        // Возвращаем корень дерева
        return root;
    }

    /**
     * Пытается разделить область (вершину BSP дерева) на две.
     * В случае успеха, область разделяется.
     *
     * @param node вершина BSP дерева
     * @return true - если удалось разделить область. В этом случае область уже будет разделена на две
     *     false - если НЕ получилось разделить.
     */
    @SuppressWarnings("MagicNumber")
    private boolean successfulSplit(BspTree node) {
        // 90% шанс на разделение области
        return ThreadLocalRandom.current().nextInt(1, 100) > CHANCE_TO_SUCCESSFUL_SPLIT
            && node.split();
    }
}
