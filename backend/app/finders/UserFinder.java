package finders;

import io.ebean.ExpressionList;
import io.ebean.Finder;
import models.User;
import utils.Moment;
import utils.comparators.TravellerComparatorForNationalitites;
import utils.comparators.TravellerComparatorForType;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserFinder extends Finder<Long, User> {

    public UserFinder(){
        super(User.class);
    }

    public Optional<User> findByAuthToken(String token) {
        if(token == null) {
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
        ExpressionList<User> query = query().fetch("nationalities", "name").fetch("travellerTypes", "name").where().and();
        if (fname != null) query.ieq("first_name", fname);
        if (lname != null) query.ieq("last_name", lname);
        if (gender != null) query.ieq("gender", gender);
        if (nationalities != null) query.in("nationality.name", nationalities);
        if (traveller_types != null) query.in("traveller_type.name", traveller_types);
        if (orderBy != null && !orderBy.toLowerCase().equals("type") && !orderBy.toLowerCase().equals("nationality")) {
            String order = sanitiseOrderBy(orderBy);
            query.setOrderBy(order);
        }
        // if confused by next line, check moment findDOBFromAge documentation, shows wht I am passign maxAge as first param and minAge as second
//        query.raw("date_of_birth BETWEEN ? AND ?", Moment.findDOBFromAge(maxAge), Moment.findDOBFromAge(minAge));
        query.endAnd();
        List<User> travellers = query.findList();
        if (orderBy != null) {
            if (orderBy.equals("nationality")) {
                Collections.sort(travellers, new TravellerComparatorForNationalitites());
            } else if (orderBy.equals("type")) {
                Collections.sort(travellers, new TravellerComparatorForType());
            }
        }
        return travellers;
    }

    private String sanitiseOrderBy(String orderBy) {
        boolean isValidOrderBy = orderBy.toLowerCase().equals("fname") ||
                orderBy.toLowerCase().equals("mname") ||
                orderBy.toLowerCase().equals("lname") ||
                orderBy.toLowerCase().equals("dob") ||
                orderBy.toLowerCase().equals("age") ||
                orderBy.toLowerCase().equals("nationality");
        if (isValidOrderBy) {
            if (orderBy.equals("dob")) {
                return  orderBy + " desc";
            }
            return orderBy.toLowerCase();
        }
        return null;
    }

}
