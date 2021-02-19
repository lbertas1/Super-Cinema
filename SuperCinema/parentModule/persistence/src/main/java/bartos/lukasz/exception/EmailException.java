package bartos.lukasz.exception;

import java.util.Arrays;

public class EmailException extends RuntimeException {
    public EmailException(StackTraceElement[] message) {
        super(Arrays.toString(message));
    }

}
