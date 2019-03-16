package models;

import finders.UserFinder;
import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Constraint;
import java.util.Date;
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


    public String authToken;

    @NotNull
    @Constraints.Required
    public int dateOfBirth;

    @NotNull
    @Constraints.Required
    public String gender;

    @NotNull
    @Constraints.Required
    @Constraints.Email
    public String email;

    @NotNull
    @Constraints.Required
    public String password;

    @NotNull
    @Constraints.Required
    public int timestamp;

    @Column(columnDefinition = "integer default 0")
    public int accountType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Nationality> nationalities;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TravellerType> types;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Trip> trips;


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

}
