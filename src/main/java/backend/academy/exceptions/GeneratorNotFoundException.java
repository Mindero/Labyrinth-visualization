package backend.academy.exceptions;

public class GeneratorNotFoundException extends Exception {
    private static final String MSG = "Такого генератора не существует";

    public GeneratorNotFoundException() {
        super(MSG);
    }

    public GeneratorNotFoundException(Throwable cause) {
        super(MSG, cause);
    }
}
