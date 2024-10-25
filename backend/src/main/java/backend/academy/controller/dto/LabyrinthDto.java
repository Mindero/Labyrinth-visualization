package backend.academy.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LabyrinthDto(@JsonProperty int height, @JsonProperty int width,
                           @JsonProperty String generatorName,
                           @JsonProperty boolean idealMaze) {
}
