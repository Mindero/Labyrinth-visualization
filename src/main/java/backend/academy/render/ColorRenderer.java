package backend.academy.render;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Выводит в консоль лабиринт и кратчайший путь с помощью ANSI кодов.
 */
public class ColorRenderer implements Renderer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WALL = "\u001B[40m"; // Black background
    public static final String ANSI_PASSAGE = "\u001B[47m"; // White background
    public static final String ANSI_START_CELL = "\u001B[44m"; // Blue background
    public static final String ANSI_END_CELL = "\u001B[41m"; // Red background
    public static final String ANSI_PATH = "\u001B[42m"; // Green background

    public ColorRenderer() {

    }

    /* Переводит двумерный массив, полученный преобразованием
       каждой клетки в соответствующий цвет, в одну строку
    */
    private static String renderToString(String[][] output) {
        StringBuilder result = new StringBuilder();
        for (String[] strings : output) {
            for (String string : strings) {
                result.append(string);
            }
            result.append('\n');
        }
        return result.toString();
    }

    /*
      Заполняет границу стенами
     */
    private static void fillBorders(String[][] output) {
        // Левая и права линия
        for (int j = 0; j < output.length; ++j) {
            output[j][0] = setColor(ANSI_WALL, '|');
            output[j][output[j].length - 1] = setColor(ANSI_WALL, '|');
        }
        // Верхняя и нижняя линия
        for (int i = 0; i < output[0].length; ++i) {
            output[0][i] = setColor(ANSI_WALL, '-');
            output[output.length - 1][i] = setColor(ANSI_WALL, '-');
        }
    }

    /**
     * Отдает строку с заданным символом и заданным цветом
     *
     * @param ANSI_color цвет в ANSI коде
     * @param character  символ
     * @return Возвращает строку с символом {@code character}, определённого цвета {@code ANSI_color}
     */
    @SuppressWarnings("ParameterName")
    private static String setColor(String ANSI_color, Character character) {
        return ANSI_color + character + ANSI_RESET;
    }

    @SuppressWarnings("ParameterName")
    private static String setColor(String ANSI_color) {
        return setColor(ANSI_color, ' ');
    }

    /**
     * Переводит каждую клетку лабиринта в соответствующий цвет
     *
     * @param maze - лабиринт
     * @return Двумерный массив строк. Каждый элемент - цвет
     */
    private String[][] getRenderMaze(Maze maze) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        String[][] output = new String[height + 2][width + 2];
        for (String[] strings : output) {
            Arrays.fill(strings, setColor(ANSI_PASSAGE));
        }
        fillBorders(output);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int realI = i + 1;
                int realJ = j + 1;
                if (maze.getCell(i, j).cellType().equals(Cell.Type.WALL)) {
                    output[realI][realJ] = setColor(ANSI_WALL);
                }
            }
        }
        return output;
    }

    @Override
    public String render(Maze maze) {
        String[][] output = getRenderMaze(maze);
        return renderToString(output);
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        String[][] renderMaze = getRenderMaze(maze);
        for (Coordinate coordinate : path) {
            renderMaze[coordinate.row() + 1][coordinate.column() + 1] = setColor(ANSI_PATH);
        }
        // Красим стартовую и конечную вершину в особые цвета
        Coordinate start = path.getFirst();
        renderMaze[start.row() + 1][start.column() + 1] = setColor(ANSI_START_CELL);

        Coordinate end = path.getLast();
        renderMaze[end.row() + 1][end.column() + 1] = setColor(ANSI_END_CELL);

        return renderToString(renderMaze);
    }

    @Override
    public List<String> solverRender(Maze maze, List<Coordinate> solverSequence) {
        String[][] renderMaze = getRenderMaze(maze);
        List<String> mazeSequence = new ArrayList<>();
        for (Coordinate coordinate : solverSequence) {
            renderMaze[coordinate.row() + 1][coordinate.column() + 1] = setColor(ANSI_PATH);
            mazeSequence.add(renderToString(renderMaze));
        }
        return mazeSequence;
    }
}
