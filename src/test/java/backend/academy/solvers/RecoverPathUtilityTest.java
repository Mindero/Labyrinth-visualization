package backend.academy.solvers;

import backend.academy.maze.cell.Coordinate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RecoverPathUtilityTest {

    @Test
    void recoverPathReturnNothing() {
        Map<Coordinate, Coordinate> pr = new HashMap<>();

        List<Coordinate> actualPath =
            RecoverPathUtility.recoverPath(new Coordinate(0, 0), new Coordinate(1, 1), pr);

        assertThat(actualPath).isEqualTo(List.of());
    }

    @Test
    void recoverPath() {
        Map<Coordinate, Coordinate> pr = new HashMap<>() {{
            put(new Coordinate(1, 1), new Coordinate(0, 1));
            put(new Coordinate(0, 1), new Coordinate(0, 0));
        }};

        List<Coordinate> expectedList =
            List.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(1, 1));
        List<Coordinate> actualPath =
            RecoverPathUtility.recoverPath(new Coordinate(0, 0), new Coordinate(1, 1), pr);

        assertThat(actualPath).isEqualTo(expectedList);
    }
}
