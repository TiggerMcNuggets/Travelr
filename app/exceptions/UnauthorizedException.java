package exceptions;

import static play.mvc.Results.unauthorized;

public class UnauthorizedException extends RestException {
    public UnauthorizedException(String message) {
        super(unauthorized(message));
    }
}
