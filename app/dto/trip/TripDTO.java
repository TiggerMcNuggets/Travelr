package dto.trip;

import dto.user.UserSimpleDTO;
import models.Trip;
import models.TripDestination;
import play.data.validation.Constraints;

import javax.validation.Constraint;
import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object used for the Trip Endpoints
 */
public class TripDTO {

    @Constraints.Required
    public long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String description;


    public UserSimpleDTO user;
    public List<TripDestinationDTO> destinations;
    public boolean published;

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.description = trip.getDescription();
        this.name = trip.getName();
        this.published = trip.isPublished();
        this.destinations = new ArrayList<>();
        for (TripDestination dest: trip.getDestinations()) {
            this.destinations.add(new TripDestinationDTO(dest));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public UserSimpleDTO getUser() {
        return user;
    }

    public void setUser(UserSimpleDTO user) {
        this.user = user;
    }

    public List<TripDestinationDTO> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<TripDestinationDTO> destinations) {
        this.destinations = destinations;
    }
}
