package backend.academy.exceptions;

public class RenderNotFoundException extends Exception {
    private static final String MSG = "Такого рендера не существует";

    public RenderNotFoundException() {
        super(MSG);
    }

    public RenderNotFoundException(Throwable cause) {
        super(MSG, cause);
    }
}
