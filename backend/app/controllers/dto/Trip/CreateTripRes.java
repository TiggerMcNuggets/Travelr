package controllers.dto.trip;

public class CreateTripRes {
    private Long id;

    public CreateTripRes(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}