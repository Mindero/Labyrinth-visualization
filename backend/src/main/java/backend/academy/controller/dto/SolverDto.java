package backend.academy.controller.dto;

import backend.academy.maze.cell.Coordinate;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SolverDto(@JsonProperty String solverName, @JsonProperty MazeDto mazeDto, @JsonProperty Coordinate start
                       , @JsonProperty Coordinate end, @JsonProperty List<Coordinate> obstacles){
}
