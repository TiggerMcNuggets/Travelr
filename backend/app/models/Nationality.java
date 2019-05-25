package models;


import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Model used to translate the Nationality class with the database structure
 * Containing the nationality name and if the nationality still exists
 */
@Entity
public class Nationality extends BaseModel {

    /**
     * Ad-hoc finder used to communicate with the database
     */
    public static final Finder<Long, Nationality> find = new Finder<>(Nationality.class);

    /**
     * Indicates if the nationality currently exists
     */
    @NotNull
    @Column(columnDefinition = "boolean default 0")
    public boolean is_old;

    /**
     * The nationality name
     */
    @Constraints.Required
    public String name;

    public Nationality(String name) {
        this.name = name;
        this.is_old = false;
    }
}
