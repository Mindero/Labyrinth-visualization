package backend.academy.controller.dto;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MazeDto(@JsonProperty boolean[][] maze) {
    public static MazeDto fromMaze(Maze maze){
        boolean[][] cells = new boolean[maze.getHeight()][maze.getWidth()];
        for (int i = 0; i < cells.length; ++i){
            for (int j = 0; j < cells[i].length; ++j){
                cells[i][j] = maze.cellIsWall(i, j);
            }
        }
        return new MazeDto(cells);
    }
    public static Maze convertToMaze (MazeDto mazeDto){
        boolean[][] cells = mazeDto.maze;
        int height = cells.length;
        int width = cells[0].length;
        Cell[][] mazeCells = new Cell[height][width];
        for (int i = 0; i < cells.length; ++i){
            for (int j = 0; j < cells[i].length ; ++j){
                mazeCells[i][j] = new Cell(i, j, !cells[i][j]);
            }
        }
        return new Maze(height, width, mazeCells);
    }
}
