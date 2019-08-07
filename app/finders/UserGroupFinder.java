package finders;

import io.ebean.ExpressionList;
import io.ebean.Finder;
import models.UserGroup;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


public class UserGroupFinder extends Finder<Long, UserGroup> {

    public UserGroupFinder(){
        super(UserGroup.class);
    }

    /**
     * retrieves all users and their information from database
     * @return
     */
    public List<UserGroup> findAll() {
        return query().findList();
    }

}
