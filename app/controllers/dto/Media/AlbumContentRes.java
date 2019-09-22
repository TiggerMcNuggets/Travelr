package controllers.dto.Media;

import models.Media;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class AlbumContentRes {
    public List<MediaRes> getMediaItems() {
        return mediaItems;
    }

    private List<MediaRes> mediaItems;
    public AlbumContentRes(List<Media> media, User user) {
        List<MediaRes> mediaItems = new ArrayList<>();
        for (Media mediaItem: media) {
            boolean canAccessPrivate = user.isAdmin() || mediaItem.getUser().getId().equals(user.getId());
            if (mediaItem.getIs_public() || canAccessPrivate) {
                MediaRes res = new MediaRes(mediaItem);
                mediaItems.add(res);
            }
        }
        this.mediaItems = mediaItems;
    }
}
