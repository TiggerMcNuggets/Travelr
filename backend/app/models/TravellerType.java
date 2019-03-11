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
    public String tType;

    @ManyToOne
    public Traveller traveller;

    public TravellerType(@Constraints.Required String tType, Traveller traveller) {
        this.tType = tType;
        this.traveller = traveller;
    }

    public String getType() {
        return tType;
    }

    public void setType(String type) {
        this.tType = type;
    }

    public Traveller getTraveller() {
        return traveller;
    }

    public void setTraveller(Traveller traveller) {
        this.traveller = traveller;
    }
}
