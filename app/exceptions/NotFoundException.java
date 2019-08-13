package exceptions;

public class NotFoundException extends CustomException {
    /**
     * Constructor
     * @param message The additional message to display with the response
     */
    public NotFoundException(String message) { super(404, message); }
}
