package exceptions;

import play.mvc.Result;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class RestException extends RuntimeException {

    private Result result;

    public RestException(Result result) {
        super();
        this.result = result;
    }

    public CompletableFuture<Result> getResult() {
        return completedFuture(result);
    }
}
