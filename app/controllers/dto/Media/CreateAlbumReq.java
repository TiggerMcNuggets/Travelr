package controllers.dto.Media;

public class CreateAlbumReq {
    private String name;

    public CreateAlbumReq(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
