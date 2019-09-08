package dto.trip;

import models.TripStatus;
import play.data.validation.Constraints;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class TripStatusDTO {

    @Constraints.Required
    @Enumerated(EnumType.STRING)
    public TripStatus status;

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

}
