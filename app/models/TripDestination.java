package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import finders.TripDestinationFinder;
import io.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Constraint;

/**
 * Ebean entity model for the TripDestination table.
 */
@Entity
public class TripDestination extends BaseModel {

    public static final TripDestinationFinder find = new TripDestinationFinder();

    @ManyToOne
    public Trip trip;

    @ManyToOne
    @Constraints.Required
    public Destination destination;

    @ManyToOne
    public TripDestination parent;

    // If ordinal is null then it is not part of the main trip. i.e it is still in the planning stage
    public int ordinal;

    // Custom name for a trip destination eg. "Adam's Flat" instead of 100 Riccarton Road. Only seen within a trip
    public String customName;

    public int arrivalDate;

    public int departureDate;


    public TripDestination(Trip trip, Destination destination) {
        this.trip = trip;
        this.destination = destination;
    }

    // used in recursion
    public TripDestination(TripDestination parent, int ordinal, String customName, int arrivalDate, int departureDate) {
        this.parent = parent;
        this.ordinal = ordinal;
        this.customName = customName;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "TripDestination{" +
                "trip=" + trip +
                ", destination=" + destination +
                ", parent=" + parent +
                ", ordinal=" + ordinal +
                ", customName='" + customName + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                '}';
    }

    // GETTERS AND SETTERS

    public TripDestination getParent() {
        return parent;
    }

    public void setParent(TripDestination parent) {
        this.parent = parent;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(int arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
    }
}
