package models;


import play.data.validation.Constraints;
import finders.NationalityFinder;
import javax.persistence.*;

@Entity
public class Nationality extends BaseModel {

    // finder linked as a public static field
    public static final NationalityFinder find = new NationalityFinder();

    @Constraints.Required
    public String name;


    public Nationality(String name) {

        this.name = name;

    }
}
