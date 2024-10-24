package backend.academy.controller;

import backend.academy.exceptions.GeneratorNotFoundException;
import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.exceptions.RenderNotFoundException;
import backend.academy.exceptions.SolverNotFoundException;
import backend.academy.generators.BSPgenerator.BspGenerator;
import backend.academy.generators.DfsGenerator;
import backend.academy.generators.Generator;
import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import backend.academy.render.ColorRenderer;
import backend.academy.render.ConsoleRenderer;
import backend.academy.render.Renderer;
import backend.academy.solvers.BfsSolver;
import backend.academy.solvers.DijkstraSolver;
import backend.academy.solvers.Solver;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleController {
    private static final Map<String, Class<?>> ALL_GENERATORS = new HashMap<>() {{
        put("DFS", DfsGenerator.class);
        put("BSP", BspGenerator.class);
    }};
    private static final Map<String, Class<?>> ALL_SOLVERS = new HashMap<>() {{
        put("Дейкстра", DijkstraSolver.class);
        put("BFS", BfsSolver.class);
    }};
    private static final Map<String, Class<?>> ALL_RENDERS = new HashMap<>() {{
        put("Цветной", ColorRenderer.class);
        put("Символьный", ConsoleRenderer.class);
    }};

    public ConsoleController() {
    }

    // Получить Generator по вводу из консоли
    public static Generator getGenerator(PrintStream out, Scanner scanner) throws GeneratorNotFoundException {
        try {
            out.println("Введите способ генерации лабиринта: " + ALL_GENERATORS.keySet());
            String generator = scanner.next();
            Class<?> generatorClass = ALL_GENERATORS.get(generator);
            return (Generator) generatorClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new GeneratorNotFoundException(ex);
        }
    }

    // Получить Solver из консоли
    public static Solver getSolver(PrintStream out, Scanner scanner) throws SolverNotFoundException {
        try {
            out.println("Введите способ нахождения кратчайшего пути в лабиринте: " + ALL_SOLVERS.keySet());
            String solver = scanner.next();
            Class<?> solverClass = ALL_SOLVERS.get(solver);
            return (Solver) solverClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new SolverNotFoundException(ex);
        }
    }

    // Получить Render из консоли
    public static Renderer getRender(PrintStream out, Scanner scanner) throws RenderNotFoundException {
        try {
            out.println("Введите способ отображения лабиринта: " + ALL_RENDERS.keySet());
            String renderer = scanner.next();
            Class<?> rendererClass = ALL_RENDERS.get(renderer);
            return (Renderer) rendererClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RenderNotFoundException(ex);
        }
    }

    public static Maze readAndGenerateMaze(PrintStream out, Scanner scanner)
        throws GeneratorNotFoundException, LabyrinthSizeSmallException {
        // Получаем размеры лабиринта
        out.println("Введите два целых числа: ширину и длину лабиринта");
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        out.println("Какой лабиринт сгенерировать?\n1 - Идеальный\n2 - Неидеальный");
        char labyrinthType = scanner.next().charAt(0);
        // Получаем генератор
        Generator mazeGenerator = getGenerator(out, scanner);
        // Получаем лабиринт
        Maze maze;
        // Если пользователь хочет идеальный лабиринт
        if (labyrinthType == '1') {
            maze = mazeGenerator.generateIdealMaze(height, width);
        } else if (labyrinthType == '2') {
            // Если хочет неидеальный лабиринт
            maze = mazeGenerator.generateNotIdealMaze(height, width);
        } else {
            throw new RuntimeException("В качестве входного параметра ожидается 1 или 2");
        }
        return maze;
    }

    public void start(InputStream in, PrintStream out) {
        Scanner scanner = new Scanner(in);
        while (true) {
            try {
                // Получаем render
                Renderer renderer = getRender(out, scanner);
                // Получаем лабиринт по данным, которые ввёл пользователь
                Maze maze = readAndGenerateMaze(out, scanner);
                // Выводим этот лабиринт
                out.println(renderer.render(maze));
                out.println("\n\n\n\n\n");
                // Получаем solver
                Solver solver = getSolver(out, scanner);
                Coordinate start = maze.getLeftTopPassageCell();
                Coordinate end = maze.getRightBottomPassageCell();
                // Получаем путь из левой верхней свободной клетки до правой нижней свободной клетки
                var path = solver.findPath(maze, start, end);
                // Выводим его
                out.println(renderer.render(maze, path));
                // Если пользователь хочет, то выводим последовательность действий алгоритма
                out.println("Хотите увидеть как работал алгоритм?\n1 - ДА\n2 - нет");
                char solverDescription = scanner.next().charAt(0);
                if (solverDescription == '1') {
                    List<String> solverSequence =
                        renderer.solverRender(maze, solver.algorithmSimulate(maze, start, end));
                    solverSequence.forEach(x -> out.println(x + "\n\n\n\n"));
                }
                break;
            } catch (Exception ex) {
                out.println(ex.getMessage());
                if (ex.getCause() != null) {
                    out.println(ex.getCause());
                }
            }
        }
    }
}
