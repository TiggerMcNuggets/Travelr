package utils;

import exceptions.BadRequestException;
import exceptions.CustomException;
import play.mvc.Result;

import static play.mvc.Results.status;

public class AsyncHandler {

    /**
     * Function for handling the async code
     * @param result the result of the async code passed in
     * @param ex any errors thrown by the code
     * @return the result unless there is an error
     */
    public static Result handleResult(Result result, Throwable ex) {
        if(ex != null) {
            try {
                if (CustomException.class.isAssignableFrom(Class.forName(ex.getMessage()))) {
                    CustomException exception = (CustomException) ex.getCause();
                    return status(exception.getResult(), exception.getResultMessage());
                }

            } catch (ClassNotFoundException e) {
                System.out.println("Could not get exception class");
            }

            ex.printStackTrace();
            return status(500);
        }
        return result;
    }
}
