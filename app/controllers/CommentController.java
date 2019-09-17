package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Comment.CreateCommentReq;
import controllers.dto.Comment.AddEmojiReq;
import models.Comment;
import models.TripNode;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;
import service.CommentService;
import repository.UserRepository;
import service.TripService;
import utils.AsyncHandler;
import javax.inject.Inject;
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
     * Adds a user emoji react to a comment.
     * @param request The HTTP request.
     * @param userId The user id
     * @param tripId The trip id
     * @param commentId The comment id
     * @return 201 response if created successfully
     */
    @Authorization.RequireAuth
    public CompletionStage<Result> addUserEmoji(Http.Request request, Long userId, Long tripId, Long commentId) {
        Form<AddEmojiReq> addEmojiReqForm = formFactory.form(AddEmojiReq.class).bindFromRequest(request);

        if (addEmojiReqForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.COMMENT_BAD_REQUEST));
        }

        AddEmojiReq addEmojiReq = addEmojiReqForm.get();

        CompletionStage<Comment> commentStage = commentRepository.getCommentHandler(commentId);
        CompletionStage<TripNode> tripStage = tripService.getTripByIdHandler(tripId);
        CompletionStage<User> userStage = userRepository.getUserHandler(userId);
        CompletionStage<Void> permissionStage = tripStage.thenCombineAsync(userStage, (tripNode, user) -> tripService.checkReadPermissionHandler(tripNode, user).join());

        // Get comment without nesting
        CompletionStage<Comment> combineStage = permissionStage.thenCombineAsync(commentStage, (permission, comment) -> comment);
        CompletionStage<Long> insertStage = combineStage.thenCombineAsync(userStage, (comment, user) -> commentRepository.addEmoji(addEmojiReq, comment, user).join());

        return insertStage.thenApplyAsync(id -> {
            JsonNodeFactory jsonFactory = JsonNodeFactory.instance;

            ObjectNode res = jsonFactory.objectNode();
            res.put("id", id);
            return created(res);
        }).handle(AsyncHandler::handleResult);
    }

}
