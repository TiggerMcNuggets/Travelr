package controllers.dto.Destination;

import play.data.validation.Constraints;

import java.util.List;

public class CreateDestinationEditReq {

    @Constraints.Required
    public long destinationId;

    public List<Long> travellerTypeIds;

}
