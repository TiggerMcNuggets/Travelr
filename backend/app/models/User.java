package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import controllers.UserController;
import controllers.dto.User.CreateUserReq;
import finders.UserFinder;
import io.ebean.annotation.NotNull;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
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

    @JsonIgnore
    public String token;

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
    @Column(length = 64)
    private byte[] password;

    public String userProfilePhoto;

    @JsonIgnore
    @NotNull
    public int timestamp;

    @JsonIgnore
    @Column(columnDefinition = "integer default 0")
    public int accountType;

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<UserNationality> nationalities;

    @ManyToMany
    public List<TravellerType> travellerTypes;


    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<Trip> trips;


    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<Destination> destinations;

    public String setToken() {
        this.token = UUID.randomUUID().toString();
        save();
        return this.token;
    }

    // TODO: delete this
    public String setToken(String token) {
        this.token = token;
        return token;
    }

    public String getToken() {
        return this.token;
    }



    public void deleteAuthToken() {
        token = null;
        save();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }


    public void setPassword(String password) {
        this.password = getSha512(password);
    }

    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public User(CreateUserReq request) {
        this.firstName = request.firstName;
        this.middleName = request.middleName;
        this.lastName = request.lastName;
        setEmail(request.email);
        setPassword(request.password);
        this.gender = request.gender;
        this.dateOfBirth = request.dateOfBirth;
        this.accountType = request.accountType;
    }

    public User(String first, String last, String email, int dob) {
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.dateOfBirth = dob;
        this.gender = "Male";
    }

}
