package exceptions;

import play.mvc.Result;

public class CustomException extends RuntimeException {

    String message;
    int result;

    /**
     * Constructor
     * @param result The http status number
     * @param message The additional message to display with the response
     */
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
