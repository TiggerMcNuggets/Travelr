package controllers.dto.Trip;

import models.TripDestination;

public class TripDestinationRes {
    private Long id;
    private String name;
    private int ordinal;
    private Integer arrivalDate;
    private Integer departureDate;

    public TripDestinationRes(TripDestination tripDestination) {
        this.id = tripDestination.destination.id;
        this.name = tripDestination.destination.name;
        this.ordinal = tripDestination.ordinal;
        this.arrivalDate = tripDestination.arrivalDate;
        this.departureDate = tripDestination.departureDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public Integer getArrivalDate() {
        return arrivalDate;
    }

    public Integer getDepartureDate() {
        return departureDate;
    }
}
