package exceptions;

public class NotFoundException extends RuntimeException {

    private String loggerOutput;

    public NotFoundException(String message) {
        this.loggerOutput = message;
    }

    public String getLoggerMessage() {
        return loggerOutput;
    }
}
