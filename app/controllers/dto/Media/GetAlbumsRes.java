package controllers.dto.Media;

import models.Album;

import java.util.ArrayList;
import java.util.List;

import models.Media;
import play.mvc.Http;

public class GetAlbumsRes {
    private List<GetAlbumRes> getAlbumRes;

    public GetAlbumsRes(Http.Request request, List<Album> albums) {
        this.getAlbumRes = new ArrayList<GetAlbumRes>();
        for (Album album : albums) {
            this.getAlbumRes.add(new GetAlbumRes(request, album));
        }
    }

    public List<GetAlbumRes> getGetAlbumRes() {
        return getAlbumRes;
    }

}
