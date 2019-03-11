package models;

import models.Destination;
import models.Trip;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Computer entity managed by Ebean
 */
@Entity
public class TripDestination extends BaseModel {

    @ManyToOne
    public Trip trip;

    @ManyToOne
    public Destination destination ;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date arrivalDate;

    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date departureDate;

    public int orderNo;

    public TripDestination(Trip trip, Destination destination, Date arrivalDate, Date departureDate, int orderNo) {
        this.trip = trip;
        this.destination = destination;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.orderNo = orderNo;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Trip getTrip() {
        return trip;
    }

    public Destination getDestination() {
        return destination;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public void orderNoSubtract() { this.orderNo -= 1; }
}
