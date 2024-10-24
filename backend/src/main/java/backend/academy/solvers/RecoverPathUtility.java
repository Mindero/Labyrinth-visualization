package backend.academy.solvers;

import backend.academy.maze.cell.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Утилитный класс для восстановления пути из первой координаты до второй по сохраненным предкам
 */
public class RecoverPathUtility {
    private RecoverPathUtility() {
    }

    // Восстановление пути из end в start
    public static List<Coordinate> recoverPath(Coordinate start, Coordinate end, Map<Coordinate, Coordinate> pr) {
        // Нет пути от end до start
        if (!pr.containsKey(end)) {
            return new ArrayList<>();
        }
        List<Coordinate> path = new ArrayList<>();
        // Идём с конца по предкам до начала
        Coordinate current = end;
        while (!current.equals(start)) {
            path.add(current);
            current = pr.get(current);
        }
        path.add(start);
        return path.reversed();
    }
}
