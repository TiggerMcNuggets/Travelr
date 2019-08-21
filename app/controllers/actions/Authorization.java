package controllers.actions;


import controllers.SecurityController;
import controllers.constants.APIResponses;
import models.User;
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
    }

    /**
     * checks if user is logged in and/or user is admin
     */
    public static class RequireAuthAction extends Action<RequireAuth> {
        public CompletionStage<Result> call(Http.Request req) {
            String authToken;
            try {
                authToken = req.header(SecurityController.AUTH_TOKEN_HEADER).get();
            } catch (Exception e) {
                return CompletableFuture.completedFuture(unauthorized(APIResponses.NOT_LOGGED_IN_ACCESS_DENIED));
            }
            if(authToken != null) {
                Optional<User> user = models.User.find.findByAuthToken(authToken);
                if (user.isPresent()) {
                    return delegate.call(req.addAttr(Attrs.USER, user.get()).addAttr(Attrs.IS_USER_ADMIN, user.get().accountType > 0));
                }
            }
            return CompletableFuture.completedFuture(unauthorized(APIResponses.NOT_LOGGED_IN_ACCESS_DENIED));
        }
    }

    @With(RequireAdminAuthAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireAdminAuth {
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
                return CompletableFuture.completedFuture(unauthorized(APIResponses.NOT_LOGGED_IN_ACCESS_DENIED));
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

            return CompletableFuture.completedFuture(unauthorized(APIResponses.NOT_LOGGED_IN_ACCESS_DENIED));
        }
    }

    @With(RequireAuthOrAdminAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireAuthOrAdmin {
    }

    /**
     * Authorization checks if the User the request is being sent for is the user
     * that is authorized (or if the user is Admin)
     *
     */
    public static class RequireAuthOrAdminAction extends Action<RequireAuth> {
        public CompletionStage<Result> call(Http.Request req) {
            String authToken;
            try {
                authToken = req.header(SecurityController.AUTH_TOKEN_HEADER).get();
            } catch (Exception e) {
                return CompletableFuture.completedFuture(unauthorized(APIResponses.NOT_LOGGED_IN_ACCESS_DENIED));
            }
            if(authToken != null) {
                Optional<User> user = models.User.find.findByAuthToken(authToken);
                if (user.isPresent()) {

                    // The id that the request is being sent for
                    Long userIdOfRequest = getIdFromPath(req.uri());
                    User accessUser = User.find.byId(userIdOfRequest);

                    if(accessUser != null && (accessUser.getId() == user.get().getId() || user.get().isAdmin())) {
                        return delegate.call(req
                                .addAttr(Attrs.USER, user.get())
                                .addAttr(Attrs.IS_USER_ADMIN, user.get().accountType > 0)
                                .addAttr(Attrs.ACCESS_USER, accessUser));
                    } else {
                        return CompletableFuture.completedFuture(forbidden("You do not have authorization to access on behalf of this user"));
                    }
                }
            }

            return CompletableFuture.completedFuture(unauthorized(APIResponses.NOT_LOGGED_IN_ACCESS_DENIED));
        }
    }

    private static Long getIdFromPath(String path) {
        String output;

        output = path.substring(path.indexOf("/api/users/") + 11);
        output = output.substring(0, output.indexOf("/"));

        try {
            return Long.parseLong(output);
        } catch (NumberFormatException e) {
            System.out.println("To use this auth function the route must have api/users/:userid/");
            return null;
        }

    }


    /**
     * Checks a user with given user id exists
     * @param id
     * @return a 404 'Not Found' response
     */
    public static CompletionStage<Result> doesUserByIdExist(Long id) {
        User user = User.find.findById(id);
        if (user != null) return null;
        return CompletableFuture.completedFuture(Results.notFound("No user with id " + id));
    }

    /**
     *
     * @param isAdmin
     * @param userIdByToken id of the user that has been retrieved from db using the auth token
     * @param userIdById id of the user retrieved from db using user id
     * @return a 403 response when users are not admin and not same user, null otherwise
     */
    public static CompletionStage<Result> isUserAuthorised(Boolean isAdmin, Long userIdByToken, Long userIdById) {
        if (isAdmin) return null;
        if (userIdByToken == userIdById) return null;
        return CompletableFuture.completedFuture(Results.forbidden("Not admin and id from token does not match user id parameter"));
    }

    /**
     *
     * @param req http request
     * @param userId the id of the user we are trying to get the trip from
     * @param tripOwnerId the trip owner id
     * @return 403: users are not admin and not same user, null: No errors
     */
    public static CompletionStage<Result> isUserAuthorisedToViewTrip(Http.Request req, Long userId, Long tripOwnerId) {
        Boolean isAdmin = req.attrs().get(Attrs.IS_USER_ADMIN);
        if (!isAdmin && tripOwnerId != userId) {
            return CompletableFuture.completedFuture(Results.forbidden("Not admin nor trip owner does not match user id parameter"));
        }
        return null;
    }

    /**
     *
     * @param request
     * @param userId
     * @return 404 if not found user, 401 if user unauthorized, null otherwise
     */
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
