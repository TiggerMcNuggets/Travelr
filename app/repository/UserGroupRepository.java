package repository;

import controllers.dto.UserGroup.User.CreateUserGroupReq;
import finders.UserGroupFinder;
import models.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserGroupRepository {

    private DatabaseExecutionContext context;

    private UserGroupFinder userGroupFinder = new UserGroupFinder();

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

            UserGroup userGroup = new UserGroup(user, group, true);

            userGroup.insert();

            return userGroup.id;

        }, context);
    }

    /**
     * Gets all userGroups
     * @return completable future of list of userGroups
     */
    public CompletableFuture<List<UserGroup>> getAllGroups() {
        return supplyAsync(() -> userGroupFinder.findAll(), context);
    }
}
