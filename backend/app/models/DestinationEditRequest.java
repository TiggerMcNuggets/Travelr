package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class DestinationEditRequest extends BaseModel {

    public static final Finder<Long, DestinationEditRequest> find = new Finder<>(DestinationEditRequest.class);

    @Constraints.Required
    @ManyToOne
    @JsonManagedReference
    public User user;

    @Constraints.Required
    @ManyToOne
    public Destination destination;

    @Constraints.Required
    @ManyToMany
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
