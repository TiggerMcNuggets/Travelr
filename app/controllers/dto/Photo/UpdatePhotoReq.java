package controllers.dto.Photo;

import play.data.validation.Constraints;

public class UpdatePhotoReq {
    @Constraints.Required
    public Boolean is_public;
}
