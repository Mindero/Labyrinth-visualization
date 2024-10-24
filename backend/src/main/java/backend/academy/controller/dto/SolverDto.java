package backend.academy.controller.dto;

import backend.academy.maze.cell.Coordinate;
import com.fasterxml.jackson.annotation.JsonProperty;

public record SolverDto(@JsonProperty String solverName, @JsonProperty MazeDto mazeDto, @JsonProperty Coordinate start
                       , @JsonProperty Coordinate end){
}
