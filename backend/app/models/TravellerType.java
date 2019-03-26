package models;


import io.ebean.Finder;
import play.data.validation.Constraints;
import finders.TravellerTypeFinder;
import javax.persistence.*;

@Entity
public class TravellerType extends BaseModel {

    public static final Finder<Long, TravellerType> find = new Finder<>(TravellerType.class);

    @Constraints.Required
    public String name;

    public TravellerType(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
