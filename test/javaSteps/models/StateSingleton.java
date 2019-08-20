package javaSteps.models;

import com.google.inject.Inject;
import models.*;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

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
    private Http.RequestBuilder request;
    private Result result;
    private User user;
    private Destination destination;
    private Album album;
    private Media media;
    private Grouping group;
    private User groupMember;
    private TripNode trip;


    private ArrayList<Destination> destinationList;


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

    public Http.RequestBuilder getRequest() {
        return request;
    }

    public void setRequest(Http.RequestBuilder request) {
        this.request = request;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Grouping getGroup() {
        return group;
    }

    public void setGroup(Grouping group) {
        this.group = group;
    }

    public User getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(User groupMember) {
        this.groupMember = groupMember;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public TripNode getTrip() {
        return trip;
    }

    public void setTrip(TripNode trip) {
        this.trip = trip;
    }
}
