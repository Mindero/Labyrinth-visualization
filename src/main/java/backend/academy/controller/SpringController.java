package backend.academy.controller;

import backend.academy.controller.dto.LabyrinthDto;
import backend.academy.controller.dto.SolverDto;
import backend.academy.controller.dto.MazeDto;
import backend.academy.exceptions.GeneratorNotFoundException;
import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.exceptions.SolverNotFoundException;
import backend.academy.maze.Maze;
import backend.academy.maze.cell.Coordinate;
import backend.academy.service.LabyrinthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/labyrinth")
public class SpringController {
    private final LabyrinthService labyrinthService;
    public SpringController (LabyrinthService labyrinthService){
        this.labyrinthService = labyrinthService;
    }
    @GetMapping(path="/getGenerators")
    public List<String> getAllGenerators(){
        return labyrinthService.getAllGenerators();
    }

    @GetMapping(path="/getSolvers")
    public List<String> getAllSolvers(){
        return labyrinthService.getAllSolvers();
    }

    @PostMapping(path="/create", consumes = "application/json")
    public MazeDto labyrinth(@RequestBody LabyrinthDto labyrinthDto)
        throws GeneratorNotFoundException, LabyrinthSizeSmallException {
        Maze maze = labyrinthService.generateLabyrinth(labyrinthDto.generatorName(),
            labyrinthDto.height(), labyrinthDto.width());
        // перевод в dto....
        return MazeDto.fromMaze(maze);
    }

    @PostMapping(path="/findShortestPath", consumes = "application/json")
    public List<Coordinate> findShortestPath (@RequestBody SolverDto solverDto) throws SolverNotFoundException {
        Maze maze = MazeDto.convertToMaze(solverDto.mazeDto());
        return labyrinthService.findShortestPath(solverDto.solverName(), maze, solverDto.start()
            , solverDto.end());
    }

    @PostMapping(path="/simulateAlgo", consumes = "application/json")
    public List<Coordinate> simulateSolverAlgo(@RequestBody SolverDto solverDto) throws SolverNotFoundException {
        Maze maze = MazeDto.convertToMaze(solverDto.mazeDto());
        return labyrinthService.simulateSolverAlgo(solverDto.solverName(), maze, solverDto.start()
            , solverDto.end());
    }
}
