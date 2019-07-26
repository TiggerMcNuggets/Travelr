package dto.shared;

import play.data.validation.Constraints;

/**
 * Data transfer object used for the Trip Endpoints
 */
public class CreatedDTO {

    @Constraints.Required
    public long id;


    public CreatedDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
