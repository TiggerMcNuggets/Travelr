package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.UserGroup.CreateUserGroupReq;
import controllers.dto.UserGroup.CreateUserGroupRes;
import controllers.dto.UserGroup.AddUserToGroupReq;
import controllers.dto.UserGroup.UpdateUserGroupReq;
import models.Grouping;
import models.User;
import models.UserGroup;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserGroupRepository;
import utils.AsyncHandler;

import javax.inject.Inject;
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
    @Authorization.RequireAuth
    public CompletionStage<Result> removeGroupMember(Http.Request request, Long userId, Long groupId, Long memberId) {
        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null)
            return middlewareRes;

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
    @Authorization.RequireAuth
    public CompletionStage<Result> deleteGroup(Http.Request request, Long userId, Long groupId) {

        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, userId);
        if (middlewareRes != null)
            return middlewareRes;

        UserGroup userGroup = UserGroup.find.query().where().eq("user_id", userId).eq("group_id", groupId).findOne();

        // Can't find the user group in the database
        if(userGroup == null) {
            return completedFuture(notFound(APIResponses.GROUP_NOT_FOUND));
        }

        // The user is not the owner of the user group.
        if (userGroup != null && !userGroup.isOwner())
            return completedFuture(forbidden(APIResponses.FORBIDDEN));

        return userGroupRepository.remove(groupId).thenApplyAsync(deletedGroupId -> {
            if(deletedGroupId == null) {
                return notFound(APIResponses.GROUP_NOT_FOUND);
            }
            return ok(APIResponses.SUCCESSFUL_GROUP_DELETION);
        });
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
            if (grouping.getName().toLowerCase().equals(req.getName().toLowerCase()) && !grouping.getId().equals(grouping)) {
                return completedFuture(badRequest(APIResponses.BAD_REQUEST));
            }
        }

        return userGroupRepository.updateUserGroup(userId, groupId, isAdmin, req).thenApplyAsync(grouping -> {

            UserGroup userGroup = UserGroup.find.query().where().eq("user_id", userId).eq("group_id", groupId).findOne();

            if (grouping == null) {
                return notFound(APIResponses.GROUP_NOT_FOUND);
            }
            else if (!isAdmin && userGroup != null && !userGroup.isOwner()) {
                return forbidden(APIResponses.FORBIDDEN);
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
}
