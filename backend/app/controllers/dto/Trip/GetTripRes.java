package controllers.dto.Trip;

import models.Destination;
import models.Trip;
import models.TripDestination;

import java.util.ArrayList;
import java.util.List;

public class GetTripRes {
    private Long id;
    private String name;
    private List<TripDestinationRes> destinations;

    public GetTripRes(Trip trip) {
        this.id = trip.id;
        this.name = trip.name;
        setDestinations(trip.destinations);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<TripDestinationRes> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<TripDestination> destinations) {
        this.destinations = new ArrayList<TripDestinationRes>();
        for (TripDestination tripDestination : destinations) {
            this.destinations.add(new TripDestinationRes(tripDestination));
        }
    }
}