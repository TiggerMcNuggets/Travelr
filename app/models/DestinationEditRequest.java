package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

@Entity
public class DestinationEditRequest extends BaseModel {

    public static final Finder<Long, DestinationEditRequest> find = new Finder<>(DestinationEditRequest.class);

    @Constraints.Required
    @ManyToOne
    public User user;

    @Constraints.Required
    @ManyToOne
    public Destination destination;

    @Constraints.Required
    @ManyToMany(cascade=CascadeType.REMOVE)
    public List<TravellerType> travellerTypes;



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

    public List<TravellerType> getTravellerTypes() {
        return travellerTypes;
    }

    public void setTravellerTypes(List<TravellerType> travellerTypes) {
        this.travellerTypes = travellerTypes;
    }
}
