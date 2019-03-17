package finders;

import io.ebean.Finder;
import models.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByLogin(String email, String password) {
        // TODO: Hash Password in the future

        return query().where().eq("email", email.toLowerCase()).eq("password", getSha512(password)).findOneOrEmpty();

    }
}
