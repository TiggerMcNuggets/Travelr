package controllers.actions;


import controllers.SecurityController;
import models.User;
import play.libs.typedmap.TypedKey;
import play.mvc.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Authorization {


    @With(RequireAuthAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireAuth {
        //boolean value() default true;
    }

    public static class RequireAuthAction extends Action<RequireAuth> {
        public CompletionStage<Result> call(Http.Request req) {
            String authToken;
            try {
                authToken = req.header(SecurityController.AUTH_TOKEN_HEADER).get();
            } catch (Exception e) {
                return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
            }
            if(authToken != null) {
                Optional<User> user = models.User.find.findByAuthToken(authToken);
                if (user.isPresent()) {
                    return delegate.call(req.addAttr(Attrs.USER, user.get()).addAttr(Attrs.IS_USER_ADMIN, user.get().accountType > 0));
                }
            }

            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }

    @With(RequireAdminAuthAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireAdminAuth {
        //boolean value() default true;
    }

    /**
     * Middleware to check if user has admin privileges.
     * Returns 401 if user has no valid token or if has no admin privileges
     * Let process the request if user is admin
     */
    public static class RequireAdminAuthAction extends Action<RequireAdminAuth> {
        public CompletionStage<Result> call(Http.Request req) {
            String authToken;
            try {
                authToken = req.header(SecurityController.AUTH_TOKEN_HEADER).get();
            } catch (Exception e) {
                return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
            }
            if(authToken != null) {
                Optional<User> user = models.User.find.findByAuthToken(authToken);
                if (user.isPresent()) {
                    int accountType = user.get().accountType;
                    if (accountType > 0) {
                        return delegate.call(req.addAttr(Attrs.USER, user.get()));
                    } else {
                        return CompletableFuture.completedFuture(unauthorized("Not an admin: Access Denied"));
                    }
                }
            }

            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }

    public static CompletionStage<Result> doesUserByIdExist(Long id) {
        User user = User.find.findById(id);
        if (user != null) return null;
        return CompletableFuture.completedFuture(Results.notFound("No user with id " + id));
    }

    public static CompletionStage<Result> isUserAuthorised(Boolean isAdmin, Long userIdByToken, Long userIdById) {
        if (isAdmin) return null;
        if (userIdByToken == userIdById) return null;
        return CompletableFuture.completedFuture(Results.forbidden("Not admin and id from token does not match user id parameter"));
    }

    public static CompletionStage<Result> userIdRequiredMiddlewareStack(Http.Request request, Long userId) {

        // User not found middleware
        Boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);
        CompletionStage<Result> userExists = doesUserByIdExist(userId);
        if (userExists != null) return userExists;

        // User unauthorised middleware
        User user = request.attrs().get(Attrs.USER);
        CompletionStage<Result> isUserAuthorised = isUserAuthorised(isAdmin, userId, user.id);
        if (isUserAuthorised != null) return isUserAuthorised;
        return null;
    }
}
