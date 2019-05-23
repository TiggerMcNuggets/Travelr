package models;

import finders.PhotoFinder;
import org.checkerframework.common.aliasing.qual.Unique;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Personal Photo entity managed by Ebean
 */
@Entity
public class PersonalPhoto extends BaseModel {

    public static final PhotoFinder find = new PhotoFinder();

    @ManyToOne
    public User user;

    /**
     * The photo filename.
     */
    @NotNull
    @Constraints.Required
    @Unique
    public String photo_filename;

    /**
     * The photo filename.
     */
    @NotNull
    @Column(columnDefinition = "boolean default 0")
    public Boolean is_public = false;

    /**
     * The PersonalPhoto constructor.
     * @param user The user to be linked to the photo
     * @param photo_filename The local file path where the image is stored.
     */
    public PersonalPhoto(User user, String photo_filename) {
        this.user = user;
        this.photo_filename = photo_filename;
    }

    /**
     * Gets the user which the photo is associated with.
     * @return The user instance.
     */
    public User getTraveller() {
        return user;
    }

    /**
     * Sets the user which the photo is associated with.
     * @param traveller The user instance.
     */
    public void setTraveller(User traveller) {
        this.user = traveller;
    }

    /**
     * Gets the photo filename.
     * @return The photo file name.
     */
    public String getPhoto_filename() {
        return photo_filename;
    }

    /**
     * Sets the photo filename
     * @param photo_filename The photo filename
     */
    public void setPhoto_filename(String photo_filename) {
        this.photo_filename = photo_filename;
    }
}