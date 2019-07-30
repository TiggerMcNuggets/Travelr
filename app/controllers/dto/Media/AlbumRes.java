package controllers.dto.Media;

import models.Album;

/**
 * This class creates the response to return when a list of media is requested for albums
 */
public class AlbumRes {
    private Long id;
    private String name;

    /**
     *
     * @param album media object to store information about a media item.
     */
    public AlbumRes(Album album) {
        this.id = album.id;
        this.name = album.name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
