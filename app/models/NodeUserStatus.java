package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


enum TripStatus {
    GOING,
    NOT_GOING,
    MAYBE
}

@Entity
public class NodeUserStatus extends BaseModel {



    /**
     * The trip member
     */
    @JsonIgnore
    @ManyToOne
    public User user;

    /**
     * The associated user trip
     */
    @ManyToOne
    public Node trip;

    /**
     * The trip status
     */
    @Constraints.Required
    public TripStatus tripStatus;

    /**
     *
     * @param user the User value
     * @param trip the Trip value
     * @param tripStatus the TripStatus value
     */
    public NodeUserStatus(User user, Node trip, TripStatus tripStatus) {
        this.user = user;
        this.trip = trip;
        this.tripStatus = tripStatus;
    }

    /**
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user new User value
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return the Trip value
     */
    public Node getTrip() {
        return trip;
    }

    /**
     *
     * @param trip the new Trip value
     */
    public void setTrip(Node trip) {
        this.trip = trip;
    }

    /**
     *
     * @return the Trip value
     */
    public TripStatus getTripStatus() {
        return tripStatus;
    }

    /**
     *
     * @param tripStatus the new Trip status
     */
    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }
}
