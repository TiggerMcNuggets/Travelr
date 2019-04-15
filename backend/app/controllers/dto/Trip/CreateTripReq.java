package controllers.dto.Trip;

import play.data.validation.Constraints;

import java.util.List;

public class CreateTripReq {
    @Constraints.Required
    public String name;

    @Constraints.Required
    public List<TripDestinationReq> destinations;

    public boolean hasLessThanTwoDestinations() {
        return destinations.size() < 2;
    }

    public boolean hasSameConsecutiveDestinations() {
        for (int i = 0; i < destinations.size() - 1; i++) {
            if (destinations.get(i).id == destinations.get(i + 1).id) {
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
