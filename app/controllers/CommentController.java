package controllers;

import controllers.actions.Attrs;
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
import org.omg.CosNaming.NamingContextPackage.NotFound;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;
import scala.concurrent.Await;
import service.CommentService;
import service.TripService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.mvc.Results.*;

public class CommentController {
    @Inject
    FormFactory formFactory;

    private final CommentRepository commentRepository;
    private final TripService tripService;
    private final CommentService commentService;

    @Inject
    public CommentController(CommentRepository commentRepository, TripService tripService, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.tripService = tripService;
        this.commentService = commentService;

    }

    /**
     * Creates a comment on a trip for a user
     * @param request The http request object
     * @param userId The user's id
     * @param tripId The trip's id
     * @return 201 if all ok
     */
    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> createComment(Http.Request request, Long userId, Long tripId) {

        Form<CreateCommentReq> createCommentReqForm = formFactory.form(CreateCommentReq.class).bindFromRequest(request);

        if (createCommentReqForm.hasErrors()) {
            return CompletableFuture.completedFuture(badRequest(APIResponses.COMMENT_BAD_REQUEST));
        }

        CreateCommentReq createCommentReq = createCommentReqForm.get();
        User user = request.attrs().get(Attrs.ACCESS_USER);

        CompletionStage<TripNode> tripStage = tripService.getTripByIdHandler(tripId);
        CompletionStage<TripNode> isWritePermittedStage = tripStage.thenApplyAsync(tripNode -> {
            tripService.isPermittedToWriteHandler(tripNode, user);
            return tripNode;
        });

        CompletionStage<Long> insert = isWritePermittedStage.thenComposeAsync(tripNode -> commentRepository.insert(createCommentReq, tripNode, user));
        return insert.thenApplyAsync(id -> created(id.toString()));
    }

    @Authorization.RequireAuthOrAdmin
    public CompletionStage<Result> toggleDeleteComment(Http.Request request, Long userId, Long tripId, Long commentId) {
        User user = request.attrs().get(Attrs.ACCESS_USER);

        CompletionStage<TripNode> tripStage = tripService.getTripByIdHandler(tripId);
        CompletionStage<User> isWritePermittedStage = tripStage.thenApplyAsync(tripNode -> {
            tripService.isPermittedToWriteHandler(tripNode, user);
            return user;
        });

        CompletionStage<Comment> isUserComment = isWritePermittedStage.thenComposeAsync(
                (currentUser) -> commentService.isUserComment(commentId, currentUser));

        CompletionStage<Long> deleted = isUserComment.thenComposeAsync(commentRepository::delete);
        return deleted.thenApplyAsync(id -> created(id.toString()));
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
