package models;

import finders.TripDestinationFinder;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class TripDestination extends BaseModel {

    public static final TripDestinationFinder find = new TripDestinationFinder();

    @ManyToOne
    public Trip trip;

    @ManyToOne
    public Destination destination ;

    public Integer arrivalDate;

    public Integer departureDate;

    public String name;

    @Constraints.Required
    public int ordinal;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Integer getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Integer arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Integer getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Integer departureDate) {
        this.departureDate = departureDate;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public TripDestination(Integer arrivalDate, Integer departureDate, int ordinal, Trip trip, Destination destination) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.ordinal = ordinal;
        this.trip = trip;
        this.destination = destination;
        this.name = destination.getName();
    }
}
