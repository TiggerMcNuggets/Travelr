package exceptions;

public class ForbiddenException extends CustomException {
    /**
     * Constructor
     * @param message The additional message to display with the response
     */
    public ForbiddenException(String message) {
        super(403, message);
    }
}
