package controllers.dto.UserGroup;

import models.Grouping;
import models.User;
import models.UserGroup;
import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CreateUserGroupReq {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Constraints.Required
    public String name;

    public String description;
}
