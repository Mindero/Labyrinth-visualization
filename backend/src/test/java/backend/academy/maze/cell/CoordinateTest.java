package backend.academy.maze.cell;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CoordinateTest {

    @Test
    void copyOfCreateNewInstance() {
        Coordinate init = new Coordinate(0, 0);
        Coordinate copy = Coordinate.copyOf(init);

        assertThat(copy == init).isFalse();
    }

    @Test
    void copyOfCreateEqualInstance() {
        Coordinate init = new Coordinate(0, 0);
        Coordinate copy = Coordinate.copyOf(init);

        assertThat(copy.equals(init)).isTrue();
    }

    @Test
    void getX() {
        Coordinate init = new Coordinate(0, 1);

        assertThat(init.getX()).isEqualTo(1);
    }

    @Test
    void getY() {
        Coordinate init = new Coordinate(0, 1);

        assertThat(init.getY()).isEqualTo(0);
    }
}
