package backend.academy.controller;

import backend.academy.exceptions.GeneratorNotFoundException;
import backend.academy.exceptions.LabyrinthSizeSmallException;
import backend.academy.exceptions.RenderNotFoundException;
import backend.academy.exceptions.SolverNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchException;

class ControllerTest {
    @Test
    void getRenderThrows() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, UTF_8);
             InputStream inputTest = new ByteArrayInputStream
                 ("aboba".getBytes())) {
            Exception ex = catchException(() -> {
                Controller.getRender(out, new Scanner(inputTest));
            });
            assertThat(ex).isInstanceOf(RenderNotFoundException.class);
        }
    }

    @Test
    void getGeneratorThrows() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, UTF_8);
             InputStream inputTest = new ByteArrayInputStream
                 ("aboba".getBytes())) {
            Exception ex = catchException(() -> {
                Controller.getGenerator(out, new Scanner(inputTest));
            });
            assertThat(ex).isInstanceOf(GeneratorNotFoundException.class);
        }
    }

    @Test
    void getSolverThrows() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, UTF_8);
             InputStream inputTest = new ByteArrayInputStream
                 ("aboba".getBytes())) {
            Exception ex = catchException(() -> {
                Controller.getSolver(out, new Scanner(inputTest));
            });
            assertThat(ex).isInstanceOf(SolverNotFoundException.class);
        }
    }

    @Test
    void readAndGenerateThrowsGenerateNotFound() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, UTF_8);
             InputStream inputTest = new ByteArrayInputStream
                 (("1\n" + "1\n" + "1\n" + "aboba").getBytes())) {
            Exception ex = catchException(() -> {
                Controller.readAndGenerateMaze(out, new Scanner(inputTest));
            });
            assertThat(ex).isInstanceOf(GeneratorNotFoundException.class);
        }
    }

    @Test
    void readAndGenerateThrowsLabyrinthSizeSmallInDfsGenerator() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, UTF_8);
             InputStream inputTest = new ByteArrayInputStream
                 (("-1\n" + "1\n" + "1\n" + "DFS").getBytes())) {
            Exception ex = catchException(() -> {
                Controller.readAndGenerateMaze(out, new Scanner(inputTest));
            });
            assertThat(ex).isInstanceOf(LabyrinthSizeSmallException.class);
        }
    }

    @Test
    void readAndGenerateThrowsLabyrinthSizeSmallInBSPGenerator() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, UTF_8);
             InputStream inputTest = new ByteArrayInputStream
                 (("-1\n" + "1\n" + "2\n" + "BSP").getBytes())) {
            Exception ex = catchException(() -> {
                Controller.readAndGenerateMaze(out, new Scanner(inputTest));
            });
            assertThat(ex).isInstanceOf(LabyrinthSizeSmallException.class);
        }
    }

    @Test
    void readAndGenerateThrowsRuntimeException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream out = new PrintStream(baos, true, UTF_8);
             InputStream inputTest = new ByteArrayInputStream
                 (("10\n" + "10\n" + "aboba\n" + "BSP").getBytes())) {
            Exception ex = catchException(() -> {
                Controller.readAndGenerateMaze(out, new Scanner(inputTest));
            });
            assertThat(ex).isInstanceOf(RuntimeException.class);
        }
    }
}
