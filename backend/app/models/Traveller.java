package models;

import io.ebean.annotation.NotNull;
import play.data.format.Formats;
import play.data.validation.Constraints;
import finders.TravellerFinder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * Traveller model
 */
@Entity
public class Traveller extends BaseModel {

    private static final long serialVersionUID = 1L;

    // finder linked as a public static field
    public static final TravellerFinder find = new TravellerFinder();

    @NotNull
    @Constraints.Required
    public String fname;

    public String mname;

    @NotNull
    @Constraints.Required
    public String lname;

    @NotNull
    @Constraints.Required
    @Formats.DateTime(pattern = "yyyy-MM-dd")
    public Date dob;

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
    @Formats.DateTime(pattern = "YYYY-MM-DD HH:MM:SS")
    public Date timestamp;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<Nationality> nationalities;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<PersonalPhoto> personalPhotos;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<TravellerType> types;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<Trip> trips;



    public Traveller(
            @Constraints.Required String fname,
            String mname,
            @Constraints.Required String lname,
            @Constraints.Required Date dob,
            @Constraints.Required String gender,
            @Constraints.Required @Constraints.Email String email,
            @Constraints.Required String password,
            @Constraints.Required Date timestamp) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.timestamp = timestamp;
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
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

    public List<PersonalPhoto> getPersonalPhotos() {
        return personalPhotos;
    }

    public void setPersonalPhotos(List<PersonalPhoto> personalPhotos) {
        this.personalPhotos = personalPhotos;
    }
}
