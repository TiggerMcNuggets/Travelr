package models;

import finders.AlbumFinder;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album extends BaseModel {

    public static final AlbumFinder find = new AlbumFinder();


    @ManyToMany(cascade= CascadeType.ALL)
    public List<Media> media;

    @ManyToOne
    public User user;

    @Constraints.Required
    public String name;

    @OneToOne
    public Destination associatedDestination;

    /**
     * Whether the album is a permanent album
     */
    @Constraints.Required
    public Boolean isPermanent;


    public Album(User user, String name, Boolean isPermanent) {
        this.user = user;

        this.media = new ArrayList<>();
        this.name = name;
        this.isPermanent = isPermanent;
    }

    public void addMedia(Media media_item) {
        media.add(media_item);
    }

    public void removeMedia(Media media_item) {
        media.remove(media_item);
    }

    public List<Media> getMedia() {
        return (List<Media>) media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPermanent() {
        return isPermanent;
    }

    public void setPermanent(Boolean permanent) {
        isPermanent = permanent;
    }
}
