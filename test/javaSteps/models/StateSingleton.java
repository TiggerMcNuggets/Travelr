package javaSteps.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import play.Application;
import play.mvc.Result;

/**
 * This singleton class allows shared data between steps
 * Note: This singleton only keeps its state within its scenario
 */
public class StateSingleton {

    // The shared singleton instance
    private static StateSingleton instance;

    // Common data that needs to be shared
    @Inject
    private Application application;
    private int travellerId;
    private int destinationId;
    private String token;
    private JsonNode requestData;
    private Result result;
    private String tripId;

    /**
     * Private constructor to avoid illegal initialisation of new objects
     * The first instance needs application's dependencies injected which
     * skips getInstance() and so instance needs to be updated in this case
     */
    @Inject
    private StateSingleton() {
        instance = this;
    }

    /**
     * Gets the instance of the singleton class or creates if none exist
     * @return the new or current instance
     */
    public static StateSingleton getInstance() {
        if (instance == null)
            instance = new StateSingleton();

        return instance;
    }

    /**
     * Getters and setters
     */

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public int getTravellerId() { return travellerId; }

    public void setTravellerId(int travellerId) { this.travellerId = travellerId; }

    public void setDestinationId(int destinationId) { this.destinationId = destinationId; }

    public int getDestinationId() { return destinationId; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public JsonNode getRequestData() {
        return requestData;
    }

    public void setRequestData(JsonNode requestData) {
        this.requestData = requestData;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
}
