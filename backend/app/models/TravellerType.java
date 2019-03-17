package models;

import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Constraint;

import play.data.validation.Constraints;
/**
 * traveller type model
 */
@Entity
public class TravellerType extends BaseModel {


    @NotNull
    @Constraints.Required
    public String name;

    public TravellerType(String name) {
        this.name = name;
    }
}
