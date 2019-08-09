package finders;

import io.ebean.ExpressionList;
import io.ebean.Finder;
import models.Grouping;
import models.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


public class GroupingFinder extends Finder<Long, Grouping> {

    public GroupingFinder(){
        super(Grouping.class);
    }

    /**
     * retrieves user from database if user email matches email given
     * @param name
     * @return A grouping with the provided name
     */
    public Grouping findByName(String name) {
        return query().where().eq("name", name).findOneOrEmpty().orElse(null);
    }

}
