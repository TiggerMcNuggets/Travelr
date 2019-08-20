package dto.trip;

import play.data.validation.Constraints;

public class TripStatusDTO {

    @Constraints.Required
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String name) {
        this.status = status;
    }

}
