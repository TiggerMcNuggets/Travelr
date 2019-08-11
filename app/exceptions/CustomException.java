package exceptions;

import play.mvc.Result;

public class CustomException extends RuntimeException {

    String message;
    int result;

    public CustomException(int result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getResultMessage() {
        return message;
    }

    public int getResult() {
        return result;
    }
}
