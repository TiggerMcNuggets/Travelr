package controllers.dto.Media;

public class CreateAlbumRes {
    private Long id;

    public CreateAlbumRes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
