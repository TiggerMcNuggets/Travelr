package exceptions;

public class UnauthorizedException extends CustomException {
    /**
     * Constructor
     * @param message The additional message to display with the response
     */
    public UnauthorizedException(String message) { super(401, message); }
}
