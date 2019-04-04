package models;

import com.sun.istack.Nullable;
import controllers.dto.Trip.TripDestinationReq;
import models.Destination;
import models.Trip;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class TripDestination extends BaseModel {

    @ManyToOne
    public Trip trip;

    @ManyToOne(cascade = CascadeType.ALL)
    public Destination destination ;

    public Integer arrivalDate;

    public Integer departureDate;

    @Constraints.Required
    public int ordinal;

    public TripDestination(Integer arrivalDate, Integer departureDate, int ordinal, Trip trip, Destination destination) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.ordinal = ordinal;
        this.trip = trip;
        this.destination = destination;

    }
}
