package models;

import com.sun.javafx.UnmodifiableArrayList;
import finders.AlbumFinder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album extends BaseModel {

    public static final AlbumFinder find = new AlbumFinder();


    @ManyToMany(cascade= CascadeType.ALL)
    public List<Media> media;


    public Album() {
        this.media = new ArrayList<>();
    }

    public void addMedia(Media media_item) {
        media.add(media_item);
    }

    public void removeMedia(Media media_item) {
        media.remove(media_item);
    }

    public UnmodifiableArrayList<Media> getMedia() {
        return (UnmodifiableArrayList<Media>) media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }
}
