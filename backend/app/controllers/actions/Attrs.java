package controllers.actions;

import models.User;
import play.libs.typedmap.TypedKey;

public class Attrs {

    public static final TypedKey<User> USER = TypedKey.<User>create("user");
    public static final TypedKey<Boolean> IS_USER_ADMIN = TypedKey.create("is_user_admin");

}
