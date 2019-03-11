package models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    private String authToken;

    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
    private String emailAddress;

    @Column(length = 64, nullable = false)
    private byte[] shaPassword;


    @Transient
    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(256)
    @JsonIgnore
    // Password which is not stored in database. Only store hash
    private String password;

    public User(String email, String password) {
        setEmailAddress(email);
        setPassword(password);
    }

    public String createAuthToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        shaPassword = getSha512(password);
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEmailAddress(String email) {
        this.emailAddress = email;
    }

    public static final Finder<Long, User> find = new Finder<>(User.class);


    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }
        try  {
            return find.query().where().eq("authToken", authToken).findOne()
        }
        catch (Exception e) {
            return null;
        }
    }


}
