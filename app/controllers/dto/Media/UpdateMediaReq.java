package controllers.dto.Media;

import play.data.validation.Constraints;

public class UpdateMediaReq {
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getIs_public() {
        return is_public;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }

    public String caption;
    public Boolean is_public;
}
