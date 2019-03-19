package models;

import play.data.validation.Constraints;
import utils.finders.DestinationFinder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Personal Photo entity managed by Ebean
 */
@Entity
public class PersonalPhoto extends BaseModel {

    @ManyToOne
    public Traveller traveller;

    /**
     * The longtitude coordinatte value of the destination.
     */
    @NotNull
    @Constraints.Required
    public String photo_filename;

    /**
     * The PersonalPhoto constructor.
     *
     * @param photo_filename The local file path where the image is stored.
     */
    public PersonalPhoto(Traveller traveller, String photo_filename) {
        this.traveller = traveller;
        this.photo_filename = photo_filename;
    }

    /**
     * Gets the traveller which the photo is associated with.
     * @return The traveller instance.
     */
    public Traveller getTraveller() {
        return traveller;
    }

    /**
     * Sets the traveller which the photo is associated with.
     * @param traveller The traveller instance.
     */
    public void setTraveller(Traveller traveller) {
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