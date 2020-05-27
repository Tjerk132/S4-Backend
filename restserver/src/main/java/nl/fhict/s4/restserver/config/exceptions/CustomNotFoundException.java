package nl.fhict.s4.restserver.config.exceptions;

public class CustomNotFoundException extends RuntimeException {

    public <T> CustomNotFoundException(String className, T searchTerm) {
        super(className + " for " + searchTerm + " was not found");
    }
}
