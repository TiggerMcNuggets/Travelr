package controllers.dto.Destination;

import play.data.validation.Constraints;

public class CreateDestReq {
    @Constraints.Required
    public String name;
    @Constraints.Required
    public Double latitude;
    @Constraints.Required
    public Double longitude;
    @Constraints.Required
    public String type;
    @Constraints.Required
    public String district;
    @Constraints.Required
    public String country;

}
