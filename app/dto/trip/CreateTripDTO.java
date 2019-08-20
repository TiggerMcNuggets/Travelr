package dto.trip;

import play.data.validation.Constraints;

import javax.validation.Constraint;

public class CreateTripDTO {

    @Constraints.Required
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
