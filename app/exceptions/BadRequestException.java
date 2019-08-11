package exceptions;

import static play.mvc.Results.badRequest;

public class BadRequestException extends RestException {
    public BadRequestException(String message) {
        super(badRequest(message));
    }
}
