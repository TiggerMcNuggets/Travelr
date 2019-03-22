package models;

import controllers.dto.Trip.TripDestinationReq;
import models.Destination;
import models.Trip;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class TripDestination extends BaseModel {

    @ManyToOne
    public Trip trip;

    @ManyToOne
    public Destination destination ;

    public int arrivalDate;

    public int departureDate;

    @Constraints.Required
    public int ordinal;

    public TripDestination(int arrivalDate, int departureDate, int ordinal, Trip trip, Destination destination) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.ordinal = ordinal;
        this.trip = trip;
        this.destination = destination;

    }
}
