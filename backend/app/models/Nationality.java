package models;


import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.*;

@Entity
public class Nationality extends BaseModel {

    public static final Finder<Long, Nationality> find = new Finder<>(Nationality.class);

    @Constraints.Required
    public String name;

    public Nationality(String name) {
        this.name = name;
    }
}
