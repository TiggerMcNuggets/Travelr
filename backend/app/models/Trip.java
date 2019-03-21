package models;

import finders.TripFinder;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;


@Entity
public class Trip extends BaseModel {

    private static final long tripID = 1L;

    public static final TripFinder find = new TripFinder();

    @ManyToOne
    public User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TripDestination> destinations;

    @Constraints.Required
    public String name;

    public Trip(String name, User user) {
        this.name = name;
        this.user = user;
    }
}

