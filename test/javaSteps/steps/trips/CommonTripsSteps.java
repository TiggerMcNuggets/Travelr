package javaSteps.steps.trips;

import cucumber.api.java.en.Given;
import javaSteps.models.StateSingleton;
import models.*;

import java.util.List;
import java.util.Map;

public class CommonTripsSteps {

    StateSingleton state = StateSingleton.getInstance();


    @Given("I own the trip")
    public void i_own_the_trip(List<Map<String, String>> dataTable) {
        for (Map<String, String> tripData : dataTable) {
            TripNode newTrip = new TripNode(tripData.get("name"), state.getUser());
            newTrip.insert();
            state.setTrip(newTrip);
        }
    }

    @Given("I do not own the trip")
    public void i_do_not_own_the_trip() {
        state.setTrip(new TripNode("A New Trip", state.getUser()));
        state.getTrip().setId(10L);
        // By not inserting, the trip does not exist
        // Trip needed to be created to pass id into request uri
    }

    @Given("They own the trip")
    public void they_own_the_trip(List<Map<String, String>> dataTable) {
        for (Map<String, String> tripData : dataTable) {

            TripNode newTrip = new TripNode(tripData.get("name"), state.getUser());
            newTrip.insert();
            state.setTrip(newTrip);
        }
    }

    @Given("The trip contains the trip destinations")
    public void the_trip_contains_the_trip_nodes(List<Map<String, String>> dataTable) {
        for (Map<String, String> nodeData : dataTable) {
            String type = String.valueOf(nodeData.get("type"));
            String name = String.valueOf(nodeData.get("name"));
            Integer ordinal = Integer.valueOf(nodeData.get("ordinal"));
            Integer arrival = Integer.valueOf(nodeData.get("arrivalDate"));
            Integer departure = Integer.valueOf(nodeData.get("departureDate"));
            if (type.toLowerCase().equals("destination")) {
                Destination dest = Destination.find.findById(Long.valueOf(nodeData.get("destinationId")));
                DestinationNode node = new DestinationNode(
                        name, this.state.getUser(), dest
                );
                node.setArrivalDate(arrival);
                node.setDepartureDate(departure);
                node.setOrdinal(ordinal);
                this.state.getTrip().getNodes().add(node);
            } else {
                TripNode node = new TripNode(name, this.state.getUser());
                this.state.getTrip().getNodes().add(node);
            }
            this.state.getTrip().save();
        }
    }

    @Given("The destinations are")
    public void the_destinations_are(List<Map<String, String>> dataTable) {
        for (Map<String, String> destInfo : dataTable) {
            Destination dest = new Destination(
                    destInfo.get("name"),
                    Double.valueOf(destInfo.get("latitude")),
                    Double.valueOf(destInfo.get("longitude")),
                    destInfo.get("type"),
                    destInfo.get("district"),
                    destInfo.get("country"),
                    state.getUser());
            dest.save();
        }
    }
}
