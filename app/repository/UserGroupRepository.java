package repository;

import controllers.constants.APIResponses;
import controllers.dto.UserGroup.AddUserToGroupReq;
import controllers.dto.UserGroup.UpdateUserGroupReq;
import exceptions.*;
import models.Grouping;
import models.User;
import models.UserGroup;
import utils.AsyncHandler;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static utils.AsyncHandler.handleResult;

public class UserGroupRepository {

    private DatabaseExecutionContext context;
    private AsyncHandler asyncHandler = new AsyncHandler();

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

    /**
     * Adds user to group
     * @param userId The user's id who is calling the request
     * @param groupId The group's id
     * @param memberId The member's id who will be added to the group
     * @param isAdmin Whether the member should be an owner
     * @param req The request DTO
     * @return Void
     * @throws CustomException Exception that will return the related request
     */
    public CompletableFuture<Void> addUserToGroup(Long userId, Long groupId, Long memberId, boolean isAdmin, AddUserToGroupReq req) throws CustomException {

        return supplyAsync(() -> {

            // Get group
            Grouping group = Grouping.find.byId(groupId);
            if (group == null) throw new NotFoundException(APIResponses.GROUP_NOT_FOUND);

            // Get member
            User user = User.find.findById(memberId);
            if (user == null) throw new NotFoundException(APIResponses.GROUP_MEMBER_NOT_FOUND);

            // Check if member already belongs to group
            UserGroup memberGroup = UserGroup.find.query().where().eq("user_id", memberId).eq("group_id", groupId).findOne();
            if (memberGroup != null) throw new ForbiddenException(APIResponses.MEMBER_EXISTS_IN_GROUP);

            // Check that user is an admin or is the owner of the group
            UserGroup userGroup = UserGroup.find.query().where().eq("user_id", userId).eq("group_id", groupId).findOne();
            if ((userGroup == null || !userGroup.isOwner) && !isAdmin) throw new ForbiddenException(APIResponses.FORBIDDEN);

            // Add user to group
            UserGroup newUserGroup = new UserGroup(user, group, req.getIsOwner());
            newUserGroup.insert();

            return null;
        }, context);
    }
}
