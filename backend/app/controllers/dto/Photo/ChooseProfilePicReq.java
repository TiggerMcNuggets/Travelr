package controllers.dto.Photo;

import play.data.validation.Constraints;

public class ChooseProfilePicReq {
    @Constraints.Required
    public String photo_filename;
}
