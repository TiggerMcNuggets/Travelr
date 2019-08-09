package controllers;

import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.UserGroupRepository;

import javax.inject.Inject;
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
     * @param user_id the user id
     * @param groupId the group id for the member to be deleted from
     * @param memberId the member id to be deleted
     * @return result of operation
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> deleteGroupMember(Http.Request request, Long user_id, Long groupId, Long memberId) {
        // middleware stack
        CompletionStage<Result> middlewareRes = Authorization.userIdRequiredMiddlewareStack(request, user_id);
        if (middlewareRes != null)
            return middlewareRes;

        return userGroupRepository.remove(groupId, memberId).thenApplyAsync(deleted_user_id -> {
            //not found check, repository checks that both album and media exist
            if(deleted_user_id == null) {
                return notFound(APIResponses.GROUP_MEMBER_NOT_FOUND);
            }
            return ok(APIResponses.SUCCESSFUL_GROUP_MEMBER_DELETION);
        });
    }





}
