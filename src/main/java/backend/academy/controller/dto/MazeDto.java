package backend.academy.controller.dto;

import backend.academy.maze.Maze;
import backend.academy.maze.cell.Cell;
import backend.academy.maze.cell.Coordinate;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.units.qual.C;

public record MazeDto(@JsonProperty CellDto[][] cellDtos) {
    public static MazeDto fromMaze(Maze maze){
        CellDto[][] cells = new CellDto[maze.getHeight()][maze.getWidth()];
        for (int i = 0; i < cells.length; ++i){
            for (int j = 0; j < cells[i].length; ++j){
                cells[i][j] = new CellDto(new Coordinate(i, j), maze.cellIsWall(i, j));
            }
        }
        return new MazeDto(cells);
    }
    public static Maze convertToMaze (MazeDto mazeDto){
        CellDto[][] cells = mazeDto.cellDtos;
        int height = cells.length;
        int width = cells[0].length;
        Cell[][] mazeCells = new Cell[height][width];
        for (int i = 0; i < cells.length; ++i){
            for (int j = 0; j < cells[i].length ; ++j){
                mazeCells[i][j] = new Cell(i, j, cells[i][j].isWall());
            }
        }
        return new Maze(height, width, mazeCells);
    }
}
