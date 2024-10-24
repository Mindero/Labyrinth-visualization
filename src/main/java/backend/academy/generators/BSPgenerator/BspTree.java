package backend.academy.generators.BSPgenerator;

import backend.academy.maze.cell.Coordinate;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Реализация BSP дерева
 * <p>Вершиной BSP дерева является область. Под областью подразумевается прямоугольник.
 *
 * <p>Метод split разделяет область на две путём вертикального или горизонтального разреза.
 *
 * <p>Комната - это прямоугольник внутри заданной области.
 * Метод createRooms создает в каждом листе BSP дерева комнату.
 *
 * <p>Метод createHall создает проход между случайной комнатой
 * левого поддерева и случайной комнатой правого поддерева.
 * Проходом в данной реализации является прямоугольник
 */
public class BspTree {
    // Минимальная ширина и высота области
    public static final int MIN_SIZE = 3;

    private static final double PROPORTION_TO_ONE_WAY_SPLIT = 1.25;
    // левый верхний угол области и её размер
    private final int left;
    private final int top;
    private final int width;
    private final int height;
    // Соединения между комнатами
    private final List<Rectangle> halls;
    private BspTree leftChild;
    private BspTree rightChild;
    // комната внутри области
    private Rectangle room;

    // Создание вершины без детей, комнаты и проходов
    public BspTree(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        leftChild = null;
        rightChild = null;
        room = null;
        halls = new ArrayList<>();
    }

    // Возвращает случайную точку внутри комнаты
    private static Coordinate randomPointInRoom(Rectangle l) {
        return new Coordinate(ThreadLocalRandom.current().nextInt(l.y, l.y + l.height),
            ThreadLocalRandom.current().nextInt(l.x, l.x + l.width));
    }

    /**
     * Разделяет область на 2 области через горизонтальный или вертикальный разрез.
     * <p>Если размер по одной из координат области сильно превышает другой,
     * то разрез происходит по большей координате.
     * В остальных случаях выбор направления разреза выбирается случайно.
     *
     * <p>Точка, относительно которой происходит разрез, выбирается случайно.
     *
     * @return true, если разделение произошло успешно
     *     false иначе.
     */

    public boolean split() {
        // Если комната и так уже маленькая, то разрез делать нельзя
        if (width <= MIN_SIZE || height <= MIN_SIZE) {
            return false;
        }
        // Если уже разделили на две области
        if (leftChild != null && rightChild != null) {
            return false;
        }
        // Если ширина больше на 25% высоты, то разделяем по вертикали
        // Если высота больше на 25% ширины, то разделяем по горизонтали
        // Иначе случайно
        boolean splitHorizontal = ThreadLocalRandom.current().nextBoolean();
        if (width >= PROPORTION_TO_ONE_WAY_SPLIT * height) {
            splitHorizontal = false;
        } else if (height >= PROPORTION_TO_ONE_WAY_SPLIT * width) {
            splitHorizontal = true;
        }
        // Находим максимальный размер, на который можно разделить область
        int maxSize = (splitHorizontal ? height : width) - MIN_SIZE;
        if (maxSize <= MIN_SIZE) {
            // Если после разреза одна из областей может стать меньше допустимой
            return false;
        }
        // Выбираем случайно размер разреза
        int splittingSize = ThreadLocalRandom.current().nextInt(MIN_SIZE, maxSize);
        // Разрезаем по горизонтали
        if (splitHorizontal) {
            leftChild = new BspTree(left, top, width, splittingSize);
            rightChild = new BspTree(left, top + splittingSize, width, height - splittingSize);
        } else { // или по вертикали
            leftChild = new BspTree(left, top, splittingSize, height);
            rightChild = new BspTree(left + splittingSize, top, width - splittingSize, height);
        }
        return true; // Успешно разделили
    }

    // Функция рекурсивно создает комнаты во всех листьях поддереве
    public void createRooms() {
        // Если вершина не лист, значит в ней нельзя создать комнату
        if (leftChild != null || rightChild != null) {
            if (leftChild != null) {
                leftChild.createRooms();
            }
            if (rightChild != null) {
                rightChild.createRooms();
            }
            // создаем проход между левым и правым поддеревом
            createHall(leftChild.getRandomRoom(), rightChild.getRandomRoom());
        } else { // Иначе лист.
            // Создаем комнату внутри области.
            // Генерируем размер комнаты
            int roomHeight = ThreadLocalRandom.current().nextInt(1, height);
            int roomWidth = ThreadLocalRandom.current().nextInt(1, width);
            // Выбираем случайную позицию внутри комнаты, от которого строим комнату
            Coordinate roomPos = new Coordinate(ThreadLocalRandom.current().nextInt(0, height - roomHeight),
                ThreadLocalRandom.current().nextInt(0, width - roomWidth));
            // Создаем прямоугольник - комнату внутри области.
            room = new Rectangle(left + roomPos.getX(), top + roomPos.getY(),
                roomWidth, roomHeight);
        }
    }

    // Берём случайную комнату в поддереве вершины
    private Rectangle getRandomRoom() {
        // Если находимся в листе
        if (room != null) {
            return room;
        }
        // Иначе случайно берём комнату либо в левом поддереве, либо правом.
        if (ThreadLocalRandom.current().nextBoolean()) {
            return getLeftChild().getRandomRoom();
        } else {
            return getRightChild().getRandomRoom();
        }
    }

    // Создание прохода между двумя комнатами
    private void createHall(Rectangle l, Rectangle r) {
        // Получаем случайные точки в комнатах
        Coordinate lCoord = randomPointInRoom(l);
        Coordinate rCoord = randomPointInRoom(r);

        // Приводим всегда к одному случаю, когда lCoord не ниже rCoord
        if (rCoord.getY() > lCoord.getY()) {
            // Обычный swap
            var temp = Coordinate.copyOf(lCoord);
            lCoord = rCoord;
            rCoord = temp;
        }
        // Разница между строками левых
        int h = rCoord.getY() - lCoord.getY();
        int w = rCoord.getX() - lCoord.getX(); // Разница по
        if (h > 0) {
            throw new RuntimeException();
        }
        // Разбор всех случаев расположения этих точек
        if (h < 0) {
            // rCoord выше и левее
            if (w < 0) {
                halls.add(new Rectangle(rCoord.getX(), rCoord.getY(), Math.abs(w) + 1, 1));
                halls.add(new Rectangle(lCoord.getX(), rCoord.getY(), 1, Math.abs(h) + 1));
            } else if (w > 0) {
                // rCoord выше и правее
                halls.add(new Rectangle(lCoord.getX(), lCoord.getY(), Math.abs(w) + 1, 1));
                halls.add(new Rectangle(rCoord.getX(), rCoord.getY(), 1, Math.abs(h) + 1));
            } else {
                // rCoord выше и их x равны
                halls.add(new Rectangle(rCoord.getX(), rCoord.getY(), 1, Math.abs(h) + 1));
            }
        } else { // h == 0
            // rCoord на той же высоте и левее
            if (w < 0) {
                halls.add(new Rectangle(rCoord.getX(), rCoord.getY(), Math.abs(w) + 1, 1));
            } else if (w > 0) {
                halls.add(new Rectangle(lCoord.getX(), lCoord.getY(), w + 1, 1));
            }
        }
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BspTree getLeftChild() {
        return leftChild;
    }

    public BspTree getRightChild() {
        return rightChild;
    }

    public Rectangle getRoom() {
        return room;
    }

    public List<Rectangle> getHalls() {
        return halls;
    }
}
