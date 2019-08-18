package exceptions;

public class BadRequestException extends CustomException {
    /**
     * Constructor
     * @param message The additional message to display with the response
     */
    public BadRequestException(String message) {
        super(400, message);
    }
}
