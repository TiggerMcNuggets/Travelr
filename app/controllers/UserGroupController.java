package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.UserGroup.*;
import models.Grouping;
import controllers.dto.UserGroup.GetUserGroupRes;
import models.User;
import models.UserGroup;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserGroupRepository;
import utils.AsyncHandler;

import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import service.MailgunService;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class UserGroupController extends Controller {

    @Inject
    FormFactory formFactory;

    private final UserGroupRepository userGroupRepository;
    private final MailgunService mailgunService;

    @Inject
    public UserGroupController(UserGroupRepository userGroupRepository, MailgunService mailgunService) {
        this.userGroupRepository = userGroupRepository;
        this.mailgunService = mailgunService;
    }

    /**
     * Deletes a single group member
     * @param request the http request
     * @param userId the user id
     * @param groupId the group id for the member to be deleted from
     * @param memberId the member id to be deleted
     * @return result of operation
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> removeGroupMember(Http.Request request, Long userId, Long groupId, Long memberId) {

        User user = request.attrs().get(Attrs.USER);
        Optional<UserGroup> userGroup =
                Optional.ofNullable(
                        UserGroup.find.query().where()
                                .eq("user", user).eq("grouping_id", groupId).findOne()
                );

        if(!user.isAdmin()) {
            if(userGroup.isPresent()) {
                if(userGroup.get().isOwner()) {
                    if(user.getId() == memberId) {
                        return completedFuture(forbidden(APIResponses.FORBIDDEN));
                    }
                } else {
                    if(user.getId() != memberId) {
                        return completedFuture(forbidden(APIResponses.FORBIDDEN));
                    }
                }
            }
        }

        return userGroupRepository.remove(groupId, memberId).thenApplyAsync(deletedUserId -> {
            if(deletedUserId == null) {
                return notFound(APIResponses.GROUP_MEMBER_NOT_FOUND);
            }
            return ok(APIResponses.SUCCESSFUL_GROUP_MEMBER_DELETION);
        });
    }


    /**
     * Delete a group of users
     * @param request the http request
     * @param userId the user id
     * @param groupId the group id
     * @return whether the deletion of the group was successful in the form of a request
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> deleteGroup(Http.Request request, Long userId, Long groupId) {

        User user = request.attrs().get(Attrs.USER);

        UserGroup userGroup =
                UserGroup.find.query().where().eq("user_id", userId).eq("grouping_id", groupId).findOne();

        // The user is not the owner of the user group and not an admin.
        if (!user.isAdmin() && (userGroup == null || !userGroup.isOwner())) {
            return completedFuture(forbidden(APIResponses.FORBIDDEN));
        }

        return userGroupRepository.removeGroupAndMembers(groupId).thenApplyAsync(deletedGroupId -> {
            if(deletedGroupId == null) {
                return notFound(APIResponses.GROUP_NOT_FOUND);
            }
            return ok(APIResponses.SUCCESSFUL_GROUP_DELETION);
        });
    }

    /**
     *
     * @param request the http request
     * @param userId the user id
     * @param groupId the group id
     * @param memberId the member to promote id
     * @return 200 if the member can be promoted to group owner,
     *      403 if user is not group owner, 404 if member or user are not in group
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> promoteGroupMember(Http.Request request, Long userId, Long groupId, Long memberId) {

        User user = request.attrs().get(Attrs.USER);

        Optional<UserGroup> userGroup = UserGroup.find.findByUserAndGroupId(userId, groupId);
        Optional<UserGroup> memberGroup = UserGroup.find.findByUserAndGroupId(memberId, groupId);

        if(!memberGroup.isPresent()) {
            return completedFuture(forbidden(APIResponses.FORBIDDEN));
        }

        // The user is not the owner of the user group.
        if (!user.isAdmin() && (!userGroup.isPresent() || !userGroup.get().isOwner())) {
            return completedFuture(forbidden(APIResponses.FORBIDDEN));
        }

        return userGroupRepository.promoteUser(memberId, groupId).thenApplyAsync(group -> {
            if (group == null) {
                return notFound(APIResponses.FAILED_TO_PROMOTE);
            }
            return ok(APIResponses.SUCCESSFUL_GROUP_UPDATE);
        }).handle(AsyncHandler::handleResult);

    }

    /**
     * Updates a user group
     * @param request the http request
     * @param userId the user id
     * @param groupId the group id to update
     * @return whether the update was successful or not
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> updateUserGroup(Http.Request request, Long userId, Long groupId) {

        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        Form<UpdateUserGroupReq> updateUserGroupForm = formFactory.form(UpdateUserGroupReq.class).bindFromRequest(request);

        if (updateUserGroupForm.hasErrors()) {
            return completedFuture(badRequest("Error updating user group"));
        }


        UpdateUserGroupReq req = updateUserGroupForm.get();
        boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);

        // Bad Request check
        for (Grouping grouping : Grouping.find.all()) {
            if (grouping.getName().toLowerCase().equals(req.getName().toLowerCase()) && !grouping.getId().equals(grouping.getId())) {
                return completedFuture(badRequest(APIResponses.BAD_REQUEST));
            }
        }

        // Check to see if the person sending the request is in the group
        User user = request.attrs().get(Attrs.USER);
        UserGroup userGroup = UserGroup.find.query().where().eq("user_id", user.getId()).eq("grouping_id", groupId).findOne();

        // Check to see if grouping exists
        Grouping grouping = Grouping.find.byId(groupId);

        if (grouping == null) {
            return completedFuture(notFound(APIResponses.GROUP_NOT_FOUND));
        } else if ((!isAdmin && userGroup == null) || (userGroup != null && !userGroup.isOwner())) {
            return completedFuture(forbidden(APIResponses.FORBIDDEN));
        }

        return userGroupRepository.updateUserGroup(userId, groupId, isAdmin, req).thenApplyAsync(updatedGrouping -> {

            if (updatedGrouping == null) {
                return notFound(APIResponses.GROUP_NOT_FOUND);
            }

            return ok(APIResponses.SUCCESSFUL_GROUP_UPDATE);
        });
    }

    /**
     * Creates a new userGroup
     * @param request from http
     * @return 201 with json object of new userGroupId if successfull
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> createUserGroup(Http.Request request, Long userId) {
        // Turns the post data into a form object
        Form<CreateUserGroupReq> userGroupRequestForm = formFactory.form(CreateUserGroupReq.class).bindFromRequest(request);

        // Bad Request Check
        if (userGroupRequestForm.hasErrors()) {
            System.out.println(userGroupRequestForm.errors());
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        User user = User.find.findById(userId);

        if (user == null) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRAVELLER_NOT_FOUND));
        }

        CreateUserGroupReq req = userGroupRequestForm.get();

        //Group Name Taken Check
        if (Grouping.find.findByName(req.name) != null) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.DUPLICATE_GROUP_NAME));
        }

        return userGroupRepository.createNewGroup(req, user).thenApplyAsync(id -> {
            CreateUserGroupRes response = new CreateUserGroupRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return created(jsonResponse);
        });
    }

    /**
     * Adds user to a group
     * @param request The request object
     * @param userId The user's id who is making the request (unless admin)
     * @param groupId The group's id
     * @param memberId The member's id who is going to be added to the group
     * @return 201 if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> addUserToGroup(Http.Request request, Long userId, Long groupId, Long memberId) {
        // Middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        // Bad request check
        Form<AddUserToGroupReq> addUserToGroupForm = formFactory.form(AddUserToGroupReq.class).bindFromRequest(request);
        if (addUserToGroupForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
        }

        AddUserToGroupReq req = addUserToGroupForm.get();
        boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);

        CompletionStage<Void> addUserToGroupStage = userGroupRepository.addUserToGroup(userId, groupId, memberId, isAdmin, req);

        return addUserToGroupStage.thenApplyAsync(stage -> {
            User recipient = User.find.findById(memberId);
            mailgunService.sendAddedToGroupEmail(recipient, groupId);
            return created();
        }).handle(AsyncHandler::handleResult);
    }

    /**
     * Gets all groups that belongs to a user
     * @param request The http request
     * @param userId The user's id
     * @return 200 if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getUserGroups(Http.Request request, Long userId) {
        // Middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null) return middlewareRes;

        User user = request.attrs().get(Attrs.USER);
        CompletionStage<List<Grouping>> getUserGroupsStage;

        if (user.isAdmin()) {
            getUserGroupsStage = userGroupRepository.getAllUserGroups();
        } else {
            getUserGroupsStage = userGroupRepository.getUserGroups(userId);
        }

        return getUserGroupsStage.thenApplyAsync(groupings -> {
            List<GetUserGroupRes> response = GetUserGroupRes.parseUserGroups(groupings);
            return ok(Json.toJson(response));
        });
    }

    /**
     * Gets a singular grouping with all members
     * @param request The request object
     * @param userId The id of the user who owns the group
     * @param groupId The group id
     * @return 200 with the group data
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> getSingleGroup(Http.Request request, Long userId, Long groupId) {
        return userGroupRepository.getGroupMembers(groupId).thenApplyAsync(UserGroups -> {
            //Check to see if group exists
            if (UserGroups.size() == 0) {
                return notFound(APIResponses.GROUP_NOT_FOUND);
            }
            List<User> members = new ArrayList<User>();
            List<User> owners = new ArrayList<User>();

            boolean isGroupMember = false;

            for (UserGroup user: UserGroups) {
                if (user.getUser().getId() == request.attrs().get(Attrs.USER).getId()) {
                    isGroupMember = true;
                }
                members.add(user.getUser());
                if (user.isOwner()) {
                    owners.add(user.getUser());
                }
            }

            //Check to see if user is part of group or is an admin
            if (!isGroupMember && !request.attrs().get(Attrs.USER).isAdmin()) {
                return forbidden(APIResponses.FORBIDDEN);
            }
            GetUserGroupRes response = new GetUserGroupRes(members, UserGroup.find.byId(groupId), owners);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return ok(jsonResponse);
        });
    }


}