package dto.trip;

import play.data.validation.Constraints;

import javax.validation.Constraint;

public class CreateTripDTO {

    @Constraints.Required
    public String name;
    @Constraints.Required
    public String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
