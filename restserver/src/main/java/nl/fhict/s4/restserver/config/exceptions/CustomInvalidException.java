package nl.fhict.s4.restserver.config.exceptions;

public class CustomInvalidException extends RuntimeException {

    public CustomInvalidException(String className, String reason) {
        super("Invalid operation for " + className + " because " + reason);
    }
}
