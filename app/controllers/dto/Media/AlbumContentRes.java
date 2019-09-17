package controllers.dto.Media;

import models.Media;

import java.util.ArrayList;
import java.util.List;

public class AlbumContentRes {
    public List<MediaRes> getMediaItems() {
        return mediaItems;
    }

    private List<MediaRes> mediaItems;
    public AlbumContentRes(List<Media> media) {
        List<MediaRes> mediaItems = new ArrayList<>();
        for (Media mediaItem: media) {
            MediaRes res = new MediaRes(mediaItem);
            mediaItems.add(res);
        }
        this.mediaItems = mediaItems;
    }
}
