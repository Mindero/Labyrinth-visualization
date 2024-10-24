package backend.academy;

import backend.academy.controller.Controller;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        new Controller().start(System.in, System.out);
    }
}
