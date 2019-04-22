package controllers.dto.Trip;

import play.data.validation.Constraints;

import javax.validation.Constraint;

public class TripDestinationReq {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
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

    @Constraints.Required
    public Long id;

    @Constraints.Required
    public int ordinal;

    public Integer arrivalDate;

    public Integer departureDate;

}
