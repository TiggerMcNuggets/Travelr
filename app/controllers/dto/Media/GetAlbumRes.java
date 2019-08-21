package controllers.dto.Media;

import models.Media;
import models.Album;
import models.User;

import controllers.actions.Attrs;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Http;

public class GetAlbumRes {
    private Long id;
    private String name;
    private Boolean is_permanant;
    private Boolean is_public;
    private List<MediaRes> content;

    public GetAlbumRes(Http.Request request, Album album) {
        this.id = album.getId();
        this.name = album.getName();
        this.is_permanant = album.getPermanent();
        this.is_public = this.findPrivacy(album.getMedia());
        this.setContent(request, album.getMedia());
    }

    public void setContent(Http.Request request, List<Media> media) {
        User user = request.attrs().get(Attrs.USER);
        Boolean isAdmin = request.attrs().get(Attrs.IS_USER_ADMIN);

        this.content = new ArrayList<>();
        for (Media mediaItem : media) {
            if (mediaItem.getIs_public() || (user.getId()).equals(mediaItem.getUser().getId()) || isAdmin) {
                this.content.add(new MediaRes(mediaItem));
            }
        }
    }

    public Boolean findPrivacy(List<Media> media) {
        for (Media mediaItem : media) {
            if (mediaItem.getIs_public()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Boolean return the isPermanant
     */
    public Boolean isIsPermanant() {
        return is_permanant;
    }

    /**
     * @return List<MediaRes> return the content
     */
    public List<MediaRes> getContent() {
        return content;
    }

     /**
     * @return Boolean returns if the album is private
     */
    public Boolean isIsPublic() {
        return is_public;
    }

}
