package models;

import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * Computer entity managed by Ebean
 */
@Entity
public class Trip extends BaseModel {

    private static final long tripID = 1L;

    @ManyToOne(cascade= CascadeType.ALL)
    public Traveller traveller;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    public List<TripDestination> destinations;

    @Constraints.Required
    public String name;

    public Trip(String name, Traveller traveller) {
        this.name = name;
        this.traveller = traveller;
    }

    public long getID() {
        return tripID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }
}
