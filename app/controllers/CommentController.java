package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Comment.CreateCommentReq;
import controllers.dto.Media.UpdateMediaReq;
import dto.trip.CommentDTO;
import exceptions.ForbiddenException;
import exceptions.NotFoundException;
import models.Comment;
import models.TripNode;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;
import scala.concurrent.Await;
import service.CommentService;
import repository.UserRepository;
import service.TripService;
import utils.AsyncHandler;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static play.mvc.Results.*;


public class CommentController {
    @Inject
    FormFactory formFactory;

    private final CommentRepository commentRepository;
    private final TripService tripService;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Inject
    public CommentController(CommentRepository commentRepository, TripService tripService, UserRepository userRepository, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.tripService = tripService;
        this.commentService = commentService;

        this.userRepository = userRepository;
    }

    /**
     * Creates a comment on a trip for a user
     * @param request The http request object
     * @param userId The user's id
     * @param tripId The trip's id
     * @return 201 if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> createComment(Http.Request request, Long userId, Long tripId) {
        Form<CreateCommentReq> createCommentReqForm = formFactory.form(CreateCommentReq.class).bindFromRequest(request);

        if (createCommentReqForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.COMMENT_BAD_REQUEST));
        }

        CreateCommentReq createCommentReq = createCommentReqForm.get();

        CompletionStage<TripNode> tripStage = tripService.getTripByIdHandler(tripId);
        CompletionStage<User> userStage = userRepository.getUserHandler(userId);
        CompletionStage<Void> permissionStage = tripStage.thenCombineAsync(userStage, (tripNode, user) -> tripService.checkWritePermissionHandler(tripNode, user).join());

        // Get tripNode without nesting
        CompletionStage<TripNode> combineStage = permissionStage.thenCombineAsync(tripStage, (permission, tripNode) -> tripNode);
        CompletionStage<Long> insertStage = combineStage.thenCombineAsync(userStage, (tripNode, user) -> commentRepository.insert(createCommentReq, tripNode, user).join());

        return insertStage.thenApplyAsync(id -> {
            JsonNodeFactory jsonFactory = JsonNodeFactory.instance;
            ObjectNode res = jsonFactory.objectNode();
            res.put("id", id);
            return created(res);
        }).handle(AsyncHandler::handleResult);
    }

    /**
     * Deletes a comment on a trip for a user
     * @param request The http request object
     * @param userId The user's id
     * @param tripId The trip's id
     * @param commentId The comment's id
     * @return 200 if all ok
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> toggleDeleteComment(Http.Request request, Long userId,Long tripId, Long commentId) {
        CompletionStage<TripNode> tripStage = tripService.getTripByIdHandler(tripId);
        CompletionStage<User> userStage = userRepository.getUserHandler(userId);
        CompletionStage<User> permissionStage = tripStage.thenCombineAsync(userStage, (tripNode, user) -> {
            tripService.checkWritePermissionHandler(tripNode, user).join();
            return user;
        });

        CompletionStage<Comment> isUserComment = permissionStage.thenComposeAsync(
                (currentUser) -> commentService.isUserComment(commentId, currentUser));

        CompletionStage<Long> deleted = isUserComment.thenComposeAsync(commentRepository::delete);
        return deleted.thenApplyAsync(id -> ok(id.toString())).handle(AsyncHandler::handleResult);
    }

    /**
     * returns a
     * @param request the request containing two option query parameters, page and comments
     * @param tripId the id of the trip object
     * @param userId the id of the user
     * @return a 200 http response when the comments for a specific page of a trip is obtained, 401 for unauthorised
     * and 404 for when the trip cannot be found
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> fetchTripComments(Http.Request request, Long tripId, Long userId) {

        Integer page;
        Integer comments;
        try {
            page = Integer.parseInt(request.getQueryString("page"));
            comments = Integer.parseInt(request.getQueryString("comments"));
        } catch (Error e) {
            page = 1;
            comments = 10;
        } catch (Exception e) {
            page = 1;
            comments = 10;
        }
        User user = request.attrs().get(Attrs.ACCESS_USER);
        Optional<TripNode> tripNode = Optional.ofNullable(TripNode.find.byId(tripId));

        if (!tripNode.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
        }
        if (!tripService.isPermittedToRead(tripNode.get(), user).join()) {
            return CompletableFuture.completedFuture(forbidden(APIResponses.FORBIDDEN));
        }

        List<Comment> commentList = Comment.find.findByTripAndPageAndComments(TripNode.find.byId(tripId), page, comments);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment: commentList) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setUserId(comment.getUser().getId());
            commentDTO.setUserFirstName(comment.getUser().getFirstName());
            commentDTO.setUserLastName(comment.getUser().getLastName());
            commentDTO.setProfilePhoto(comment.getUser().getUserProfilePhoto());
            commentDTO.setComment(comment.getMessage());
            commentDTOList.add(commentDTO);

        }
        return CompletableFuture.completedFuture(ok(Json.toJson(commentDTOList)));
    }

}
