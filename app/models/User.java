package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import controllers.dto.User.CreateUserReq;
import finders.UserFinder;
import io.ebean.annotation.NotNull;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
    @Column(length = 150)
    public String firstName;

    @Column(length = 64)
    public String middleName;

    @NotNull
    @Constraints.Required
    @Column(length = 150)
    public String lastName;

    @JsonIgnore
    public String token;

    @NotNull
    @Constraints.Required
    public int dateOfBirth;


    @NotNull
    @Constraints.Required
    public String gender;

    @Column(length = 150, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
    public String email;

    @JsonIgnore
    @Column(length = 128)
    private byte[] password;
    public String userProfilePhoto;

    @JsonIgnore
    @NotNull
    public Long timestamp;

    @JsonIgnore
    @Column(columnDefinition = "integer default 0")
    public int accountType;

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<UserNationality> nationalities;

    /**
     * Groups which the user belongs to
     */
    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<UserGroup> userGroups;

    @ManyToMany(cascade=CascadeType.ALL)
    public List<TravellerType> travellerTypes;

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<PersonalPhoto> personalPhotos;

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<DestinationPhoto> destinationPhotos;

    @OneToMany(cascade=CascadeType.ALL)
    public List<Album> albums;

    @OneToMany(cascade=CascadeType.ALL,orphanRemoval=true)
    public List<Destination> destinations;

    // Returns true if the user is an admin
    public boolean isAdmin() {
        return this.accountType > 0;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static UserFinder getFind() {
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

    public String getEmail() {
        return email;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<UserNationality> getNationalities() {
        return nationalities;
    }

    public void setNationalities(List<UserNationality> nationalities) {
        this.nationalities = nationalities;
    }

    public List<TravellerType> getTravellerTypes() {
        return travellerTypes;
    }

    public void setTravellerTypes(List<TravellerType> travellerTypes) {
        this.travellerTypes = travellerTypes;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public String setToken() {
        this.token = UUID.randomUUID().toString();
        save();
        return this.token;
    }

    public String setToken(String token) {
        this.token = token;
        return token;
    }

    public String getToken() {
        return this.token;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
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

    public Album getDefaultAlbum() {
        for(Album albumObj: this.getAlbums()) {
            if (albumObj.getPermanent()) {
                return albumObj;
            }
        }
        return null;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public User(CreateUserReq request) {
        this.firstName = request.firstName;
        this.middleName = request.middleName;
        this.lastName = request.lastName;
        setEmail(request.email);
        setPassword(request.password);
        this.userProfilePhoto = "defaultPic.png";
        this.gender = request.gender;
        this.dateOfBirth = request.dateOfBirth;
        this.accountType = request.accountType;
        Date date = new Date();
        this.timestamp = date.getTime() / 1000;
        this.albums = new ArrayList<>();
        Album defaultAlbum =  new Album(this, "All", true);
        this.albums.add(defaultAlbum);
    }

    public User(String first, String last, String email, int dob) {
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.userProfilePhoto = "defaultPic.png";
        this.dateOfBirth = dob;
        this.gender = "Male";
        Date date = new Date();
        this.timestamp = date.getTime() / 1000;
        this.albums = new ArrayList<>();
        Album defaultAlbum =  new Album(this, "All", true);
        this.albums.add(defaultAlbum);
    }

}
