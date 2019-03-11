package models;

import io.ebean.Finder;
import io.ebean.annotation.NotNull;
import play.data.format.Formats;
import play.data.validation.Constraints;
import utils.finders.TravellerFinder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Constraint;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Traveller model
 */
@Entity
public class Traveller extends BaseModel {

    private static final long serialVersionUID = 1L;

    public static final Finder<Long, Traveller> find = new Finder<>(Traveller.class);

    @NotNull
    @Constraints.Required
    public String firstName;

    public String middleName;

    @NotNull
    @Constraints.Required
    public String lastName;

    @NotNull
    @Constraints.Required
    public int dateOfBirth;

    @NotNull
    @Constraints.Required
    public String gender;

    @NotNull
    @Constraints.Required
    public int timestamp;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<Nationality> nationalities;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<TravellerType> types;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<Trip> trips;

    public Traveller(@Constraints.Required String firstName, String middleName, @Constraints.Required String lastName, @Constraints.Required int dateOfBirth, @Constraints.Required String gender, @Constraints.Required int timestamp, List<Nationality> nationalities, List<TravellerType> types, List<Trip> trips) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.timestamp = timestamp;
        this.nationalities = nationalities;
        this.types = types;
        this.trips = trips;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static Finder<Long, Traveller> getFind() {
        return find;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(int dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<Nationality> getNationalities() {
        return nationalities;
    }

    public void setNationalities(List<Nationality> nationalities) {
        this.nationalities = nationalities;
    }

    public List<TravellerType> getTypes() {
        return types;
    }

    public void setTypes(List<TravellerType> types) {
        this.types = types;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}
