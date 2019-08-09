package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.constants.APIResponses;
import controllers.dto.UserGroup.CreateUserGroupReq;
import controllers.dto.UserGroup.CreateUserGroupRes;
import models.User;
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
     * Creates a new userGroup
     * @param request from http
     * @return 201 with json object of new userGroupId if successfull
     */
//    @Authorization.RequireAuthOrAdmin
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

        //Group Name Take Check
        return userGroupRepository.createNewGroup(req, user).thenApplyAsync(id -> {
            if (id == null) {
                return badRequest("Group name is already taken");
            }
            CreateUserGroupRes response = new CreateUserGroupRes(id);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.valueToTree(response);
            return created(jsonResponse);
        });

    }

}
