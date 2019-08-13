package controllers.dto.UserGroup;

import play.data.validation.Constraints;

public class AddUserToGroupReq {

    @Constraints.Required
    public boolean isOwner;

    public boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean owner) {
        isOwner = owner;
    }
}
