package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.*;

@Entity
public class TravellerType extends BaseModel {

    public static final Finder<Long, TravellerType> find = new Finder<>(TravellerType.class);

    @JsonIgnore
    @ManyToMany
    public User user;

    @JsonIgnore
    @ManyToMany
    public Destination destination;

    @Constraints.Required
    public String name;

    public TravellerType(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
