package models;


import com.sun.istack.NotNull;
import play.data.validation.Constraints;
import utils.finders.NationalityFinder;
import javax.persistence.*;
import java.util.Comparator;

@Entity
public class Nationality extends BaseModel {

    // finder linked as a public static field
    public static final NationalityFinder find = new NationalityFinder();

    @Constraints.Required
    public String nationality;

    @Column(columnDefinition = "boolean not null default 1")
    public Integer hasPassport;

    public Nationality(String nationality, Integer hasPassport) {

        this.nationality = nationality;
        this.hasPassport = hasPassport;
    }
}
