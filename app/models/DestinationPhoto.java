package models;

import finders.DestinationPhotoFinder;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Personal Photo entity managed by Ebean
 */
@Entity
public class DestinationPhoto extends BaseModel {

    public static final DestinationPhotoFinder find = new DestinationPhotoFinder();

    @ManyToOne
    public User user;

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    @ManyToOne
    public Destination destination;

    /**
     * The photo filename.
     */
    @NotNull
    @Constraints.Required
    public String photo_filename;

    /**
     * The photo filename.
     */
    @NotNull
    @Column(columnDefinition = "boolean default 0")
    public Boolean is_public = false;

    /**
     * The DestinationPhoto constructor.
     *
     * @param photo_filename The local file path where the image is stored.
     */
    public DestinationPhoto(User traveller, String photo_filename, Destination destination) {
        this.user = traveller;
        this.photo_filename = photo_filename;
        this.destination = destination;
    }

    /**
     * Gets the user which the photo is associated with.
     * @return The user instance.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user which the photo is associated with.
     * @param user The user instance.
     */
    public void setUser(User user) {
        this.user = user;
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