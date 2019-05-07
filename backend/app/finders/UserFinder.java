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

    public Optional<User> findByAuthToken(String token) {
        if (token == null) {
            return null;
        }
        return query().where().eq("token", token).findOneOrEmpty();
    }

    private byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByLogin(String email, String password) {
        return query().where().eq("email", email.toLowerCase()).eq("password", getSha512(password)).findOneOrEmpty();

    }

    public List<User> findAll() {
        return query().fetch("nationalities").fetch("nationalities.nationality").fetch("travellerTypes").findList();
    }

    public User findById(Long id) {
        return query().fetch("nationalities").fetch("nationalities.nationality").fetch("travellerTypes").where().eq("id", id).findOneOrEmpty().orElse(null);
    }

    public User findByEmail(String email) {
        return query().where().eq("email", email.toLowerCase()).findOneOrEmpty().orElse(null);
    }

    public List<User> findUsersByParams(String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities, List<String> traveller_types, String orderBy) {
        ExpressionList<User> query = query().where().and();
        if (fname != null) query.ieq("first_name", fname);
        if (lname != null) query.ieq("last_name", lname);
        if (gender != null) query.ieq("gender", gender);

        query.endAnd();
        return query.findList();
    }

    private String sanitiseOrderBy(String orderBy) {
        boolean isValidOrderBy = orderBy.equalsIgnoreCase("fname") ||
                orderBy.equalsIgnoreCase("mname") ||
                orderBy.equalsIgnoreCase("lname") ||
                orderBy.equalsIgnoreCase("dob") ||
                orderBy.equalsIgnoreCase("age") ||
                orderBy.equalsIgnoreCase("nationality");
        if (isValidOrderBy) {
            if (orderBy.equals("dob")) {
                return  orderBy + " desc";
            }
            return orderBy.toLowerCase();
        }
        return null;
    }

}
