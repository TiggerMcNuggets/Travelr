package controllers;

import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import models.UserGroup;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserGroupRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserGroupController extends Controller {

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
            //not found check, repository checks that both album and media exist
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

        return userGroupRepository.remove(groupId).thenApplyAsync(deletedUserGroupId -> {
            //not found check, repository checks that both album and media exist
            if(deletedUserGroupId == null) {
                return notFound(APIResponses.GROUP_NOT_FOUND);
            }
            return ok(APIResponses.SUCCESSFUL_GROUP_DELETION);
        });
    }





}
