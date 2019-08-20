package models;

import finders.TripFinder;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.util.List;

/**
 * Ebean entity model for Trip table.
 */
@Entity
public class Trip extends BaseModel {

    public static final TripFinder find = new TripFinder();

    @ManyToOne
    public User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<TripDestination> destinations;

    @Constraints.Required
    public String name;

    public String description;

    @Column(columnDefinition = "boolean default 0")
    public boolean published;

    /**
     * The user group associated with the trip
     */
    @ManyToOne
    public Grouping userGroup;

    public Trip(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    // GETTERS AND SETTERS

    public List<TripDestination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<TripDestination> destinations) {
        this.destinations = destinations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Grouping getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Grouping userGroup) {
        this.userGroup = userGroup;
    }
}

