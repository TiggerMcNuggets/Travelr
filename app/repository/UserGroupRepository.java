package repository;

import controllers.dto.UserGroup.CreateUserGroupReq;
import models.*;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserGroupRepository {

    private DatabaseExecutionContext context;

    @Inject
    public UserGroupRepository(DatabaseExecutionContext context) {
        this.context = context;
    }



    /**
     * Creates a new group for the user provided
     * @param request CreateUserGroupReq with name and description
     * @param user The user who the group is being created for
     * @return the groups Id
     */
    public CompletableFuture<Long> createNewGroup(CreateUserGroupReq request, User user) {
        return supplyAsync(() -> {
            Grouping group = new Grouping(request.name, request.description);

            group.insert();

            UserGroup userGroup = new UserGroup(user, group, true);

            userGroup.insert();

            return userGroup.id;

        }, context);
    }
}
