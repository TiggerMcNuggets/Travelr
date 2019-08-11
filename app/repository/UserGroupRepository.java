package repository;

import controllers.dto.UserGroup.UpdateUserGroupReq;
import models.Grouping;
import models.UserGroup;

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
     * Deletes a group member
     * @param groupId The group id
     * @param memberId The id of the group member to be removed
     * @return The id of the deleted group member otherwise null if not found.
     */
    public CompletableFuture<Long> remove(Long groupId, Long memberId) {
        return supplyAsync(() -> {
            UserGroup userGroup = UserGroup.find.query().where().eq("user_id", memberId).eq("group_id", groupId).findOne();
            if (userGroup != null) {
                userGroup.delete();
                return userGroup.user.getId();
            }
            return null;
        }, context);
    }

    /**
     * Deletes a group and all its members
     * @param groupId The group id
     * @return The id of the deleted group otherwise null if not found.
     */
    public CompletableFuture<Long> remove(Long groupId) {
        return supplyAsync(() -> {
            List<UserGroup> userGroupsToBeDeleted = UserGroup.find.query().where().eq("group_id", groupId).findList();;
            for (UserGroup userGroup : userGroupsToBeDeleted) {
                userGroup.delete();
            }

            Grouping groupToBeDeleted = Grouping.find.byId(groupId);

            if (groupToBeDeleted != null) {
                groupToBeDeleted.delete();
                return groupToBeDeleted.getId();
            }

            return null;
        }, context);
    }

    /**
     * Updates a user group
     * @param userId The user's id
     * @param groupId The user group id to be updated
     * @param isAdmin Whether the user is an admin
     * @param req The request object
     * @return
     */
    public CompletableFuture<Grouping> updateUserGroup(Long userId, Long groupId, boolean isAdmin, UpdateUserGroupReq req) {
        return supplyAsync(() -> {
            Grouping group = Grouping.find.byId(groupId);

            // Not found check
            if (group == null) return null;

            // Updating the group if no problems
            group.setName(req.getName());
            group.setDescription(req.getDescription());
            group.update();

            return group;
        }, context);
    }




}
