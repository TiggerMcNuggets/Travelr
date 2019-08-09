package controllers;

import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.UserGroup.UpdateUserGroupReq;
import models.Grouping;
import models.UserGroup;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserGroupRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserGroupController extends Controller {

    @Inject
    FormFactory formFactory;

    private final UserGroupRepository userGroupRepository;

    @Inject
    public UserGroupController(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
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
    public CompletionStage<Result> deleteGroupMember(Http.Request request, Long userId, Long groupId, Long memberId) {
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
        if (userGroup != null && !userGroup.isOwner())
            return CompletableFuture.completedFuture(forbidden(APIResponses.FORBIDDEN));

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
            return CompletableFuture.completedFuture(badRequest("Error updating user group"));
        }


        UpdateUserGroupReq req = updateUserGroupForm.get();
        boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);

        // Bad Request check
        for (Grouping grouping : Grouping.find.all()) {
            if (grouping.getName().toLowerCase().equals(req.getName().toLowerCase()) && !grouping.getId().equals(grouping)) {
                return CompletableFuture.completedFuture(badRequest(APIResponses.BAD_REQUEST));
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





}
