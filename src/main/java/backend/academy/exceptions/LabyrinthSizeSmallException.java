package backend.academy.exceptions;

public class LabyrinthSizeSmallException extends Exception {

    public LabyrinthSizeSmallException(String msg) {
        super(msg);
    }

    public LabyrinthSizeSmallException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
