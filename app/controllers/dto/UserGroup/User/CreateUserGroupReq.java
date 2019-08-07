package controllers.dto.UserGroup.User;

import models.Grouping;
import models.User;
import models.UserGroup;
import play.data.validation.Constraints;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CreateUserGroupReq {

    @Constraints.Required
    public String name;

    public String description;
}
