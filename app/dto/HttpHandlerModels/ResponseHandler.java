package dto.HttpHandlerModels;

import com.google.gson.JsonObject;

public class ResponseHandler {
    private int status;
    private JsonObject body;

    public ResponseHandler(int status, JsonObject body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JsonObject getBody() {
        return body;
    }

    public void setBody(JsonObject body) {
        this.body = body;
    }
}
