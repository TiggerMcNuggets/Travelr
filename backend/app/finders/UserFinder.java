package finders;

import io.ebean.Finder;
import models.User;

import java.util.Optional;

public class UserFinder extends Finder<Long, User> {

    public UserFinder(){
        super(User.class);
    }

    public Optional<User> findByAuthToken(String token) {
        if(token == null) {
            return null;
        }

        return query().where().eq("authToken", token).findOneOrEmpty();
    }

    public Optional<User> findByLogin(String email, String password) {
        // TODO: Hash Password in the future

        return query().where().eq("email", email.toLowerCase()).eq("password", password).findOneOrEmpty();

    }
}
