package exceptions;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) { super(404, message); }
}
