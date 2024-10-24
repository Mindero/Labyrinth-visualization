package backend.academy.service;

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
import org.springframework.stereotype.Service;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class LabyrinthService {
    private static final Map<String, Class<?>> ALL_GENERATORS = new HashMap<>() {{
        put("DFS", DfsGenerator.class);
        put("BSP", BspGenerator.class);
    }};
    private static final Map<String, Class<?>> ALL_SOLVERS = new HashMap<>() {{
        put("Дейкстра", DijkstraSolver.class);
        put("BFS", BfsSolver.class);
    }};

    private static Generator getGenerator(String name) throws GeneratorNotFoundException {
        try {
            Class<?> generatorClass = ALL_GENERATORS.get(name);
            return (Generator) generatorClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new GeneratorNotFoundException(ex);
        }
    }

    // Получить Solver из консоли
    private static Solver getSolver(String name) throws SolverNotFoundException {
        try {
            Class<?> solverClass = ALL_SOLVERS.get(name);
            return (Solver) solverClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new SolverNotFoundException(ex);
        }
    }

    public List<String> getAllGenerators(){
        return ALL_GENERATORS.keySet().stream().toList();
    }
    public List<String> getAllSolvers(){
        return ALL_SOLVERS.keySet().stream().toList();
    }

    public Maze generateLabyrinth (String generatorName, int height, int width)
        throws GeneratorNotFoundException, LabyrinthSizeSmallException {
        Generator generator = getGenerator(generatorName);
        return generator.generateIdealMaze(height, width);
    }

    public List<Coordinate> findShortestPath (String solverName, Maze maze, Coordinate start, Coordinate end)
        throws SolverNotFoundException {
        Solver solver = getSolver(solverName);
        return solver.findPath(maze, start, end);
    }
    public List<Coordinate> simulateSolverAlgo (String solverName, Maze maze, Coordinate start, Coordinate end)
        throws SolverNotFoundException {
        Solver solver = getSolver(solverName);
        return solver.algorithmSimulate(maze, start, end);
    }
}
