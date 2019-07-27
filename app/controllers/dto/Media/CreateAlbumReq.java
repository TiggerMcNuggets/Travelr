package controllers.dto.Media;

import play.data.validation.Constraints;

public class CreateAlbumReq {
    @Constraints.Required
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
