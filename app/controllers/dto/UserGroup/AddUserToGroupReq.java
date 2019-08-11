package controllers.dto.UserGroup;

import play.data.validation.Constraints;

public class AddUserToGroupReq {

    @Constraints.Required
    public boolean  isOwner;

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}
