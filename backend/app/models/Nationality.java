package models;


import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Nationality extends BaseModel {

    public static final Finder<Long, Nationality> find = new Finder<>(Nationality.class);

    @NotNull
    @Column(columnDefinition = "boolean default 0")
    public boolean is_old;

    @Constraints.Required
    public String name;

    public Nationality(String name) {
        this.name = name;
        this.is_old = false;
    }

}
