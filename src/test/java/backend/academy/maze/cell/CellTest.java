package backend.academy.maze.cell;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CellTest {

    @Test
    void setPassageChangePassage() {
        Cell initCell = new Cell(0, 0, 0);
        Cell newCell = initCell.setPassage();

        assertThat(newCell.cellType()).isEqualTo(Cell.Type.PASSAGE);
    }

    @Test
    void setPassageCreateNewInstance() {
        Cell initCell = new Cell(0, 0, 0);
        Cell newCell = initCell.setPassage();

        assertThat(initCell.cellType()).isEqualTo(Cell.Type.WALL);
    }

    @Test
    void isWall() {
        Cell cell = new Cell(0, 0, 0);

        assertThat(cell.isWall()).isTrue();
    }

    @Test
    void isNotWall() {
        Cell cell = new Cell(0, 0, 1);

        assertThat(cell.isWall()).isFalse();
    }
}
