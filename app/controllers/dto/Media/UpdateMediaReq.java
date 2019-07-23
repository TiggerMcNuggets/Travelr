package controllers.dto.Media;

import play.data.validation.Constraints;

public class UpdateMediaReq {
    @Constraints.Required
    public Boolean is_public;
}
