package exceptions;

import static play.mvc.Results.forbidden;

public class ForbiddenException extends RestException {
    public ForbiddenException(String message) {
        super(forbidden(message));
    }
}
