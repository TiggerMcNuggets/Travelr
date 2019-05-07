package controllers.dto.photo;

import play.data.validation.Constraints;

public class ChooseProfilePicReq {
    @Constraints.Required
    public String photo_filename;
}
