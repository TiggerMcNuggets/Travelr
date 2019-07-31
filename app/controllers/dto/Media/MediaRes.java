package controllers.dto.Media;

import models.Album;
import models.Media;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates the response to return when a list of media is requested for albums
 */
public class MediaRes {
    private Long id;
    private String filename;
    private Boolean is_public;
    private String type;
    private String caption;
    private List<AlbumDTO> albums;

    /**
     *
     * @param media media object to store information about a media item.
     */
    public MediaRes(Media media) {
        this.id = media.id;
        this.filename = media.uriString;
        this.is_public = media.is_public;
        this.type = media.mediaType;
        this.caption = media.caption;

        this.albums = new ArrayList<>();
        for (Album album : media.albums) {
            this.albums.add(new AlbumDTO(album.name, album.id));
        }
    }

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return String return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @return Boolean return the is_public
     */
    public Boolean isIs_public() {
        return is_public;
    }

    /**
     * @return String return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return String return the caption
     */
    public String getCaption() {
        return caption;
    }

    public List<AlbumDTO> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumDTO> albums) {
        this.albums = albums;
    }
}
