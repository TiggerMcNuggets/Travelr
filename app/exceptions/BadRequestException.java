package exceptions;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(400, message);
    }
}
