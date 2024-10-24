package backend.academy.render;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Выводит в консоль лабиринт и кратчайший путь через простые символы.
 */
public class ConsoleRenderer implements Renderer {
    public ConsoleRenderer() {

    }

    /* Переводит двумерный массив, полученный преобразованием
       каждой клетки в соответствующий цвет, в одну строку
    */
    private static String renderToString(Character[][] output) {
        StringBuilder result = new StringBuilder();
        for (Character[] characters : output) {
            for (Character character : characters) {
                result.append(character);
            }
            result.append('\n');
        }
        return result.toString();
    }

    /*
        Заполняет границу стенами
    */
    private static void fillBorders(Character[][] output) {
        for (int j = 0; j < output.length; ++j) {
            output[j][0] = '|';
            output[j][output[j].length - 1] = '|';
        }
        for (int i = 0; i < output[0].length; ++i) {
            output[0][i] = '-';
            output[output.length - 1][i] = '-';
        }
    }

    /**
     * Переводит каждую клетку лабиринта в соответствующий символ
     *
     * @param maze - лабиринт
     * @return Двумерный массив строк. Каждый элемент - символ
     */
    private Character[][] getRenderMaze(Maze maze) {
        int width = maze.getWidth();
        int height = maze.getHeight();
        Character[][] output = new Character[height + 2][width + 2];
        for (Character[] characters : output) {
            Arrays.fill(characters, ' ');
        }
        fillBorders(output);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int realI = i + 1;
                int realJ = j + 1;
                if (maze.getCell(i, j).cellType().equals(Cell.Type.WALL)) {
                    output[realI][realJ] = '#';
                }
            }
        }
        return output;
    }

    @Override
    public String render(Maze maze) {
        Character[][] output = getRenderMaze(maze);
        return renderToString(output);
    }

    @Override
    public String render(Maze maze, List<Coordinate> path) {
        Character[][] renderMaze = getRenderMaze(maze);
        for (Coordinate coordinate : path) {
            renderMaze[coordinate.row() + 1][coordinate.column() + 1] = 'x';
        }
        return renderToString(renderMaze);
    }

    @Override
    public List<String> solverRender(Maze maze, List<Coordinate> solverSequence) {
        Character[][] renderMaze = getRenderMaze(maze);
        List<String> mazeSequence = new ArrayList<>();
        for (Coordinate coordinate : solverSequence) {
            renderMaze[coordinate.row() + 1][coordinate.column() + 1] = 'x';
            mazeSequence.add(renderToString(renderMaze));
        }
        return mazeSequence;
    }
}
