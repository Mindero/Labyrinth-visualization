package backend.academy.controller.dto;

import backend.academy.maze.cell.Coordinate;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CellDto(@JsonProperty Coordinate coordinate, @JsonProperty boolean isWall) {
}
