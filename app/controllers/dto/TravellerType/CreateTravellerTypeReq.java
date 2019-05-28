package controllers.dto.TravellerType;

import play.data.validation.Constraints;

public class CreateTravellerTypeReq {

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Long id;

}
