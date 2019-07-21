package models;

import org.checkerframework.common.aliasing.qual.Unique;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Photo extends Media {

    /**
     * The photo filename.
     */
    @NotNull
    @Constraints.Required
    @Unique
    public String photo_filename;

    /**
     * Default constructor for photo
     * @param is_public Whether the media is public
     * @param user The user the media is associated with.
     * @param photo_filename The filename associated with the photo
     */
    public Photo(@NotNull Boolean is_public, User user, String photo_filename, List<Album> albums) {
        this.is_public = is_public;
        this.user = user;
        this.photo_filename = photo_filename;
        this.albums = albums;
    }


    public String getPhoto_filename() {
        return photo_filename;
    }

    public void setPhoto_filename(String photo_filename) {
        this.photo_filename = photo_filename;
    }
}
