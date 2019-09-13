package controllers;

import controllers.actions.Attrs;
import controllers.actions.Authorization;
import controllers.constants.APIResponses;
import controllers.dto.Comment.CreateCommentReq;
import controllers.dto.Media.UpdateMediaReq;
import exceptions.ForbiddenException;
import exceptions.NotFoundException;
import models.Comment;
import models.TripNode;
import models.User;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;
import service.CommentService;
import service.TripService;

import javax.inject.Inject;
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
    public CompletionStage<Result> toggleDeleteComment(Http.Request request, Long userId,Long tripId, Long commentId) {
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

}
