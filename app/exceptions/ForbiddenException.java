package exceptions;

public class ForbiddenException extends CustomException {
    public ForbiddenException(String message) {
        super(403, message);
    }
}
