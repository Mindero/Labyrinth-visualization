package backend.academy.generators.BSPgenerator;

import java.awt.Rectangle;
import java.util.List;
import org.junit.jupiter.api.RepeatedTest;
import static org.assertj.core.api.Assertions.assertThat;

class BspTreeTest {

    @RepeatedTest(100)
    void splitHorizontalOrVertical() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        BspTree leftChild = root.getLeftChild();
        BspTree rightChild = root.getRightChild();

        assertThat(leftChild).satisfiesAnyOf(
            // Разрез по горизонтали
            v -> assertThat(v.getHeight()).isEqualTo(rightChild.getHeight()).isEqualTo(90),
            // Разрез по вертикали
            v -> assertThat(v.getWidth()).isEqualTo(rightChild.getWidth()).isEqualTo(100)
        );
    }

    @RepeatedTest(100)
    void sumOfsplittedAreasSizeHaveEqualParentAreaSize() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        BspTree leftChild = root.getLeftChild();
        BspTree rightChild = root.getRightChild();

        assertThat(leftChild).satisfiesAnyOf(
            // Разрез по горизонтали
            v -> {
                assertThat(v.getHeight()).isEqualTo(rightChild.getHeight()).isEqualTo(90);
                assertThat(v.getWidth() + rightChild.getWidth()).isEqualTo(100);
            },
            // Разрез по вертикали
            v -> {
                assertThat(v.getWidth()).isEqualTo(rightChild.getWidth()).isEqualTo(100);
                assertThat(v.getHeight() + rightChild.getHeight()).isEqualTo(90);
            }
        );
    }

    @RepeatedTest(100)
    void splittedAreasDontOverlap() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        BspTree leftChild = root.getLeftChild();
        BspTree rightChild = root.getRightChild();

        assertThat(leftChild).satisfiesAnyOf(
            // Разрез по горизонтали
            v -> {
                assertThat(v.getHeight()).isEqualTo(rightChild.getHeight()).isEqualTo(90);
                assertThat(v.getLeft()).isEqualTo(1);
                assertThat(v.getTop()).isEqualTo(2);
                assertThat(rightChild.getTop()).isEqualTo(2);
                assertThat(rightChild.getLeft()).isEqualTo(1 + leftChild.getWidth());
            },
            // Разрез по вертикали
            v -> {
                assertThat(v.getWidth()).isEqualTo(rightChild.getWidth()).isEqualTo(100);
                assertThat(v.getLeft()).isEqualTo(1);
                assertThat(v.getTop()).isEqualTo(2);
                assertThat(rightChild.getTop()).isEqualTo(2 + v.getHeight());
                assertThat(rightChild.getLeft()).isEqualTo(1);
            }
        );
    }

    @RepeatedTest(100)
    void roomContainsInArea() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.createRooms();
        Rectangle room = root.getRoom();

        assertThat(room.x).isGreaterThanOrEqualTo(1);
        assertThat(room.y).isGreaterThanOrEqualTo(2);
        assertThat(room.x + room.width).isLessThanOrEqualTo(100 + 1);
        assertThat(room.y + room.height).isLessThanOrEqualTo(90 + 2);
    }

    @RepeatedTest(100)
    void hallsNotEmpty() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        root.createRooms();

        List<Rectangle> halls = root.getHalls();
        assertThat(halls.size()).isNotEqualTo(0);
    }

    @RepeatedTest(100)
    void hallsAtMost2() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        root.createRooms();

        List<Rectangle> halls = root.getHalls();
        assertThat(halls.size()).isLessThanOrEqualTo(2);
    }

    // Проход имеет вид 1 x width или height x 1
    @RepeatedTest(100)
    void hallsAreRectangleLine() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        root.createRooms();

        List<Rectangle> halls = root.getHalls();
        assertThat(halls).allSatisfy(hall -> {
            assertThat(hall).satisfiesAnyOf(
                h -> assertThat(h.width).isEqualTo(1),
                h -> assertThat(h.height).isEqualTo(1)
            );
        });
    }

    // Для левой области существует точка, находящаяся в проходе
    @RepeatedTest(100)
    void hallContainsInLeftChild() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        root.createRooms();
        Rectangle leftChildRoom = root.getLeftChild().getRoom();

        List<Rectangle> halls = root.getHalls();

        // Какой-то проход есть в левом сыне
        assertThat(halls).anySatisfy(hall -> {
            // Проверяем, что один из концов лежит внутри области левого сына
            assertThat(hall).satisfiesAnyOf(
                h -> {
                    assertThat(h.x).isGreaterThanOrEqualTo(leftChildRoom.x);
                    assertThat(h.y).isGreaterThanOrEqualTo(leftChildRoom.y);
                    assertThat(h.x).isLessThanOrEqualTo(leftChildRoom.x + leftChildRoom.width);
                    assertThat(h.y).isLessThanOrEqualTo(leftChildRoom.y + leftChildRoom.height);
                },
                h -> {
                    assertThat(h.x + h.width - 1).isGreaterThanOrEqualTo(leftChildRoom.x);
                    assertThat(h.y + h.height - 1).isGreaterThanOrEqualTo(leftChildRoom.y);
                    assertThat(h.x + h.width - 1).isLessThanOrEqualTo(leftChildRoom.x + leftChildRoom.width);
                    assertThat(h.y + h.height - 1).isLessThanOrEqualTo(leftChildRoom.y + leftChildRoom.height);
                }
            );
        });
    }

    // Для правой области существует точка, находящаяся в проходе
    @RepeatedTest(100)
    void hallContainsInRightChild() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        root.createRooms();
        Rectangle rightChildRoom = root.getRightChild().getRoom();

        List<Rectangle> halls = root.getHalls();

        // Какой-то проход есть в правом сыне
        assertThat(halls).anySatisfy(hall -> {
            // Проверяем, что один из концов лежит внутри области левого сына
            assertThat(hall).satisfiesAnyOf(
                h -> {
                    assertThat(h.x).isGreaterThanOrEqualTo(rightChildRoom.x);
                    assertThat(h.y).isGreaterThanOrEqualTo(rightChildRoom.y);
                    assertThat(h.x).isLessThanOrEqualTo(rightChildRoom.x + rightChildRoom.width);
                    assertThat(h.y).isLessThanOrEqualTo(rightChildRoom.y + rightChildRoom.height);
                },
                h -> {
                    assertThat(h.x + h.width - 1).isGreaterThanOrEqualTo(rightChildRoom.x);
                    assertThat(h.y + h.height - 1).isGreaterThanOrEqualTo(rightChildRoom.y);
                    assertThat(h.x + h.width - 1).isLessThanOrEqualTo(rightChildRoom.x + rightChildRoom.width - 1);
                    assertThat(h.y + h.height - 1).isLessThanOrEqualTo(rightChildRoom.y + rightChildRoom.height - 1);
                }
            );
        });
    }

    @RepeatedTest(100)
    void hallsAreConnectedToEachOther() {
        BspTree root = new BspTree(1, 2, 100, 90);
        root.split();
        root.createRooms();

        List<Rectangle> halls = root.getHalls();
        if (halls.size() == 2) {
            Rectangle hall1 = halls.get(0);

            // Координаты одних из концов совпадают
            assertThat(halls.get(1)).satisfiesAnyOf(
                hall2 -> {
                    assertThat(hall2.x).isEqualTo(hall1.x);
                    assertThat(hall2.y).isEqualTo(hall1.y);
                },
                hall2 -> {
                    assertThat(hall2.x + hall2.width - 1).isEqualTo(hall1.x);
                    assertThat(hall2.y + hall2.height - 1).isEqualTo(hall1.y);
                },
                hall2 -> {
                    assertThat(hall2.x).isEqualTo(hall1.x + hall1.width - 1);
                    assertThat(hall2.y).isEqualTo(hall1.y + hall1.height - 1);
                },
                hall2 -> {
                    assertThat(hall2.x + hall2.width - 1).isEqualTo(hall1.x + hall1.width - 1);
                    assertThat(hall2.y + hall2.height - 1).isEqualTo(hall1.y + hall1.height - 1);
                }
            );
        }
    }
}
