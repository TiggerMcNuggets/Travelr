package controllers;

import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import repository.CommentRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.mvc.Results.ok;

public class CommentController {
    @Inject
    FormFactory formFactory;

    private final CommentRepository commentRepository;

    @Inject
    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CompletionStage<Result> createComment(Http.Request request, Long userId, Long tripId) {
        return completedFuture(ok());
    }
}
