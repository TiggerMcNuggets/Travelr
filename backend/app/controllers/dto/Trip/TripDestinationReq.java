package controllers.dto.Trip;

import play.data.validation.Constraints;

import javax.validation.Constraint;

public class TripDestinationReq {

    @Constraints.Required
    public Long id;

    @Constraints.Required
    public int ordinal;

    public Integer arrivalDate;

    public Integer departureDate;
}
