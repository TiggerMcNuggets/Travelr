package exceptions;

import static play.mvc.Results.notFound;

public class NotFoundException extends RestException {
    public NotFoundException(String message) {
        super(notFound(message));
    }
}
