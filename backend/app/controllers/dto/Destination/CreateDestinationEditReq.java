package controllers.dto.Destination;

import controllers.dto.TravellerType.CreateTravellerTypeReq;
import play.data.validation.Constraints;

import java.util.List;

public class CreateDestinationEditReq {

    @Constraints.Required
    public long destinationId;

    public List<CreateTravellerTypeReq> travellerTypeIds;



}
