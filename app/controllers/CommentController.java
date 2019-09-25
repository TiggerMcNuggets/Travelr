package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Comment.CreateCommentReq;
import controllers.dto.Comment.AddEmojiReq;
import dto.trip.CommentDTO;
import dto.trip.CommentListDTO;
import models.Comment;
import models.TripNode;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;
import service.CommentService;
import repository.UserRepository;
import service.TripService;
import utils.AsyncHandler;
import javax.inject.Inject;
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
        CompletionStage<Void> permissionStage = tripStage.thenCombineAsync(userStage, (tripNode, user) -> tripService.checkReadPermissionHandler(tripNode, user).join());

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
     * returns a http response with a status of 200, 401 or 403 respectively for successfully retrieving comments,
     * when the user making the request isn't authenticated and when a user does not have permissions to retrieve the comments
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
        String ordering;
        try {
            page = Integer.parseInt(request.getQueryString("page"));
            comments = Integer.parseInt(request.getQueryString("comments"));
            ordering = request.getQueryString("ordering");
        } catch (Error e) {
            page = 0;
            comments = 5;
            ordering = "desc";
        } catch (Exception e) {
            page = 0;
            comments = 5;
            ordering = "desc";
        }
        User user = request.attrs().get(Attrs.ACCESS_USER);
        Optional<TripNode> tripNode = Optional.ofNullable(TripNode.find.byId(tripId));

        if (!tripNode.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
        }
        if (!tripService.isPermittedToRead(tripNode.get(), user).join()) {
            return CompletableFuture.completedFuture(forbidden(APIResponses.FORBIDDEN));
        }

        List<Comment> commentList = Comment.find.findByTripAndPageAndComments(TripNode.find.byId(tripId), page, comments, ordering);
        int commentCount = Comment.find.getTripCommentsCount(TripNode.find.byId(tripId));
        CommentListDTO commentListDTO = new CommentListDTO(commentList, commentCount);
        return CompletableFuture.completedFuture(ok(Json.toJson(commentListDTO)));
    }


    /**
     * returns a http response with a status of 200, 401 or 403 respectively for successfully retrieving comments,
     * when the user making the request isn't authenticated and when a user does not have permissions to retrieve the comments
     * @param request the request containing two option query parameters, page and comments
     * @param tripId the id of the trip object
     * @param userId the id of the user
     * @return a 200 http response when the comments for a specific page of a trip is obtained, 401 for unauthorised
     * and 404 for when the trip cannot be found
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> fetchTripComment(Http.Request request, Long tripId, Long userId, Long commentId) {

        User user = request.attrs().get(Attrs.ACCESS_USER);
        Optional<TripNode> tripNode = Optional.ofNullable(TripNode.find.byId(tripId));

        if (!tripNode.isPresent()) {
            return CompletableFuture.completedFuture(notFound(APIResponses.TRIP_NOT_FOUND));
        }
        if (!tripService.isPermittedToRead(tripNode.get(), user).join()) {
            return CompletableFuture.completedFuture(forbidden(APIResponses.FORBIDDEN));
        }

        Optional<Comment> comment = Comment.find.findById(commentId);
        if (!comment.isPresent()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.NOT_FOUND));
        }
        CommentDTO commentDTO = new CommentDTO(comment.get());
        return CompletableFuture.completedFuture(ok(Json.toJson(commentDTO)));
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
    public CompletionStage<Result> toggleUserEmoji(Http.Request request, Long userId, Long tripId, Long commentId) {
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
        CompletionStage<Long> insertStage = combineStage.thenCombineAsync(userStage, (comment, user) -> commentRepository.toggleEmoji(addEmojiReq, comment, user).join());

        return insertStage.thenApplyAsync(id -> {
            JsonNodeFactory jsonFactory = JsonNodeFactory.instance;

            ObjectNode res = jsonFactory.objectNode();
            res.put("id", id);
            return created(res);
        }).handle(AsyncHandler::handleResult);
    }

}
