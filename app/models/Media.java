package models;

import finders.MediaFinder;
import org.checkerframework.common.aliasing.qual.Unique;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Media extends BaseModel {

    public static final MediaFinder find = new MediaFinder();

    /**
     * is the media public or not.
     */
    @NotNull
    @Column(columnDefinition = "boolean default 0")
    public Boolean is_public = false;

    /**
     * The media caption
     */
    @Column(length = 250)
    public String caption;

    /**
     * The media URI.
     */
    @NotNull
    @Constraints.Required
    @Unique
    public String uriString;

    /**
     * The the media type.
     */
    @NotNull
    @Constraints.Required
    public String mediaType;

    /**
     * The user which the media belongs to
     */
    @ManyToOne
    public User user;

    public Boolean getIs_public() {
        return is_public;
    }

    @Constraints.Required
    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<Album> albums = new ArrayList<>();

    public Media(User user, String uriString) {
        this.uriString = uriString;
        this.user = user;
        this.mediaType = "Photo";
    }

    public Media(User user, String uriString, String caption) {
        this.uriString = uriString;
        this.user = user;
        this.mediaType = "Photo";
        this.caption = caption;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public void removeAlbum(Album album) {
        this.albums.remove(album);
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public String getCaption() {
        return caption;
    }

    public Boolean fileCanBeDeleted() {
        return this.albums.size() == 0;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
