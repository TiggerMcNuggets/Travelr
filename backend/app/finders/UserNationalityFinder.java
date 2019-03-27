package finders;

import io.ebean.Finder;
import models.UserNationality;
import models.User;
import models.Nationality;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class UserNationalityFinder extends Finder<Long, UserNationality> {

    public UserNationalityFinder(){
        super(UserNationality.class);
    }

    public UserNationality ByUserNationality(User user, Nationality nationality) {
        if(user == null || nationality == null) {
            return null;
        }

        return query().where().eq("User", user).eq("Nationality", nationality).findOne();
    }
}
