package backend.academy.exceptions;

public class SolverNotFoundException extends Exception {
    private static final String MSG = "Такого solver не существует";

    public SolverNotFoundException() {
        super(MSG);
    }

    public SolverNotFoundException(Throwable cause) {
        super(MSG, cause);
    }
}
