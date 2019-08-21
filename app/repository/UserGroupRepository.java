package repository;

import controllers.constants.APIResponses;
import controllers.dto.UserGroup.AddUserToGroupReq;
import controllers.dto.UserGroup.CreateUserGroupReq;
import controllers.dto.UserGroup.UpdateUserGroupReq;
import exceptions.*;
import models.Grouping;
import models.User;
import models.UserGroup;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
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
            return group.id;
        }, context);
    }

    /**
     * Deletes a group member
     * @param groupId The group id
     * @param memberId The id of the group member to be removed
     * @return The id of the deleted group member otherwise null if not found.
     */
    public CompletableFuture<Long> remove(Long groupId, Long memberId) {
        return supplyAsync(() -> {
            UserGroup userGroup = UserGroup.find.query().setIncludeSoftDeletes()
                    .where().eq("user_id", memberId).eq("grouping_id", groupId).findOne();
            if (userGroup != null) {
                userGroup.setDeleted(!userGroup.isDeleted());
                userGroup.update();
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
    public CompletableFuture<Long> removeGroupAndMembers(Long groupId) {
        return supplyAsync(() -> {

            Grouping groupToBeDeleted =
                    Grouping.find.findByIdWithSoftDeletes(groupId);

            if (groupToBeDeleted != null) {
                groupToBeDeleted.setDeleted(!groupToBeDeleted.isDeleted());
                groupToBeDeleted.update();
                return groupToBeDeleted.getId();
            }

            return null;
        }, context);
    }

    /**
     *
     * Gets a list of UserGroups
     * @param id the id of the group
     * @return completable future of list of userGroups
     */
    public CompletableFuture<List<UserGroup>> getGroupMembers(Long id) {
        return supplyAsync(() -> UserGroup.find.query().where().eq("grouping_id",id).findList(), context);
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
            UserGroup memberGroup = UserGroup.find.query().where().eq("user_id", memberId).eq("grouping_id", groupId).setIncludeSoftDeletes().findOne();
            if (memberGroup != null) {

                // Checks if the user is soft deleted and in the group already
                if (memberGroup.isDeleted()) {
                    memberGroup.setDeleted(!memberGroup.isDeleted());
                    memberGroup.update();
                    return null;
                } else {
                    throw new ForbiddenException(APIResponses.MEMBER_EXISTS_IN_GROUP);
                }
            }

            // Check that user is an admin or is the owner of the group
            UserGroup userGroup = UserGroup.find.query().where().eq("user_id", userId).eq("grouping_id", groupId).findOne();
            if ((userGroup == null || !userGroup.isOwner) && !isAdmin) throw new ForbiddenException(APIResponses.FORBIDDEN);

            // Add user to group
            UserGroup newUserGroup = new UserGroup(user, group, req.getIsOwner());
            newUserGroup.insert();

            return null;
        }, context);
    }

    /**
     * Gets all user groups that belongs to the user
     * @param userId The user's id
     * @return the list of groups
     * @throws CustomException
     */
    public CompletableFuture<List<Grouping>> getUserGroups(Long userId) throws CustomException {
        return supplyAsync(() -> Grouping
            .find
            .query()
            .fetch("userGroups")
            .fetch("userGroups.user")
            .fetch("userGroups.user.nationalities")
            .fetch("userGroups.user.nationalities.nationality")
            .where()
            .eq("userGroups.user.id", userId)
            .and()
            .eq("userGroups.deleted", false)
            .orderBy("userGroups.user.id")
            .findList()
        , context);
    }

    /**
     * Gets all user groups that exist and aren't deleted on the website
     * @return the list of all user groups
     * @throws CustomException
     */
    public CompletableFuture<List<Grouping>> getAllUserGroups() throws CustomException {
        return supplyAsync(() -> Grouping
            .find
            .query()
            .fetch("userGroups")
            .fetch("userGroups.user")
            .fetch("userGroups.user.nationalities")
            .fetch("userGroups.user.nationalities.nationality")
            .where()
            .eq("userGroups.deleted", false)
            .orderBy("userGroups.user.id")
            .findList()
        , context);
    }

    /**
     *
     * @param memberId
     * @param groupId
     * @return the group id if successfull, null otherwise
     */
    public CompletableFuture<Long> promoteUser(Long memberId, Long groupId) {
        return supplyAsync(() -> {
            Grouping group = Grouping.find.byId(groupId);

            // Not found check
            if (group == null) return null;

            // Check if member already belongs to group
            Optional<UserGroup> memberGroup = UserGroup.find.findByUserAndGroupId(memberId, groupId);
            if (!memberGroup.isPresent()) {
                return null;
            }

            UserGroup userGroup = memberGroup.get();

            // Check if there is less than two members
            List<UserGroup> userGroups = UserGroup
                    .find
                    .query()
                    .where()
                    .eq("grouping_id", groupId)
                    .and()
                    .eq("is_owner", true)
                    .findList();

            if (userGroups.size() < 2 && userGroup.isOwner()) throw new ForbiddenException("Cannot demote when only one owner left");

            userGroup.setOwner(!userGroup.isOwner());
            userGroup.update();
            return group.id;

            }, context);
    }
}
