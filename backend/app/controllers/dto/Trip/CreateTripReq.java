package controllers.dto.trip;

import play.data.validation.Constraints;

import java.util.List;

public class CreateTripReq {
    @Constraints.Required
    public String name;

    @Constraints.Required
    public List<TripDestinationReq> destinations;

    /**
     * returns true if trip to be created has less than 2 destinations
     * @return
     */
    public boolean hasLessThanTwoDestinations() {
        return destinations.size() < 2;
    }

    /**
     * returns true if trip to be created has less than 2 destinations
     * @return
     */
    public boolean hasSameConsecutiveDestinations() {
        for (int i = 0; i < destinations.size() - 1; i++) {
            if (destinations.get(i).getId() == destinations.get(i + 1).getId()) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TripDestinationReq> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<TripDestinationReq> destinations) {
        this.destinations = destinations;
    }
}
