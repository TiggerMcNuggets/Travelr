package models;

import finders.DestinationPhotoFinder;
import finders.PhotoFinder;
import io.ebean.Finder;
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
    public User traveller;

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
    public DestinationPhoto(User traveller, String photo_filename) {
        this.traveller = traveller;
        this.photo_filename = photo_filename;
    }

    /**
     * Gets the traveller which the photo is associated with.
     * @return The traveller instance.
     */
    public User getTraveller() {
        return traveller;
    }

    /**
     * Sets the traveller which the photo is associated with.
     * @param traveller The traveller instance.
     */
    public void setTraveller(User traveller) {
        this.traveller = traveller;
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