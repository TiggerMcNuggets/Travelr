package controllers.actions;


import controllers.SecurityController;
import models.User;
import play.libs.typedmap.TypedKey;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;

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
            String authToken = null;
            try {
                authToken = req.header(SecurityController.AUTH_TOKEN_HEADER).get();
            } catch (Exception e) {
                return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
            }
            if(authToken != null) {
                Optional<User> user = models.User.find.findByAuthToken(authToken);
                if (user.isPresent()) {
                    return delegate.call(req.addAttr(Attrs.USER, user.get()));
                }
            }

            return CompletableFuture.completedFuture(unauthorized("Not Logged In: Access Denied"));
        }
    }
}
