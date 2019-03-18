package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import controllers.UserController;
import finders.UserFinder;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User model
 */
@Entity
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    // finder linked as a public static field
    public static final UserFinder find = new UserFinder();

    @NotNull
    @Constraints.Required
    public String firstName;

    public String middleName;

    @NotNull
    @Constraints.Required
    public String lastName;


    public String authToken;

    @NotNull
    @Constraints.Required
    public int dateOfBirth;

    @NotNull
    @Constraints.Required
    public String gender;

    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
    public String email;

    @JsonIgnore
    @Column(length = 64, nullable = false)
    private byte[] shaPassword;

    @NotNull
    public int timestamp;

    @Column(columnDefinition = "integer default 0")
    public int accountType;

    @OneToMany(mappedBy="nationality")
    public List<UserNationality> nationalities;

    @ManyToMany
    public List<TravellerType> travellerTypes;

    public String setAuthToken() {
        this.authToken = UUID.randomUUID().toString();
        save();
        return this.authToken;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }


    public void setPassword(String password) {
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


    public User(UserController.CreateUserRequest request) {
        this.firstName = request.firstName;
        this.middleName = request.middleName;
        this.lastName = request.lastName;
        setEmail(request.email);
        setPassword(request.password);
        this.gender = request.gender;
        this.dateOfBirth = request.dateOfBirth;
    }

}
