package finders;

import io.ebean.ExpressionList;
import io.ebean.Finder;
import models.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


public class UserFinder extends Finder<Long, User> {

    public UserFinder(){
        super(User.class);
    }

    /**
     * retrieves user from the database if users's authorization token matches the token given
     * @param token
     * @return
     */
    public Optional<User> findByAuthToken(String token) {
        if (token == null) {
            return null;
        }
        return query().where().eq("token", token).findOneOrEmpty();
    }

    /**
     * retrieves encoded value for password
     * @param value
     * @return
     */
    private byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * retrieves user from database if user email and password matches that given
     * @param email
     * @param password
     * @return
     */
    public Optional<User> findByLogin(String email, String password) {
        return query().where().eq("email", email.toLowerCase()).eq("password", getSha512(password)).findOneOrEmpty();

    }

    /**
     * retrieves all users and their information from database
     * @return
     */
    public List<User> findAll() {
        return query().fetch("nationalities").fetch("nationalities.nationality").fetch("travellerTypes").findList();
    }

    /**
     * retrieves user and its information from database if user id matches id given
     * @param id
     * @return
     */
    public User findById(Long id) {
        return query().fetch("nationalities").fetch("nationalities.nationality").fetch("travellerTypes").where().eq("id", id).findOneOrEmpty().orElse(null);
    }

    /**
     * retrieves user from database if user email matches email given
     * @param email
     * @return
     */
    public User findByEmail(String email) {
        return query().where().eq("email", email.toLowerCase()).findOneOrEmpty().orElse(null);
    }

    /**
     * retrieves all users from the database that have first name, last name, and gender that match those given
     * @param fname
     * @param lname
     * @param gender
     * @param minAge
     * @param maxAge
     * @param nationalities
     * @param traveller_types
     * @param orderBy
     * @return
     */
    public List<User> findUsersByParams(String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities, List<String> traveller_types, String orderBy) {
        ExpressionList<User> query = query().where().and();
        if (fname != null) query.ieq("first_name", fname);
        if (lname != null) query.ieq("last_name", lname);
        if (gender != null) query.ieq("gender", gender);

        query.endAnd();
        return query.findList();
    }

    /**
     * Finds a user by id no matter if they're deleted or not
     * @param id the users id
     * @return the user
     */
    public User findByIdIncludeDeleted(Long id) {
        return query().setIncludeSoftDeletes().fetch("nationalities").fetch("nationalities.nationality").fetch("travellerTypes").where().eq("id", id).findOneOrEmpty().orElse(null);
    }


}
