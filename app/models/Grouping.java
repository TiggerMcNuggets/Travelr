package models;

import finders.GroupingFinder;
import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Grouping extends BaseModel {

    /**
     * The user grouping finder
     */
    public static final GroupingFinder find = new GroupingFinder();

    /**
     * The name of the user grouping
     */
    @Column(length = 250)
    @Constraints.Required
    public String name;

    /**
     * The description of the user grouping
     */
    @Column(length = 300)
    public String description;

    /**
     * The user groups
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<UserGroup> userGroups;

    /**
     * Associated trips for the group.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Node> associatedTrips;


    /**
     * The constructor for the user grouping
     * @param name The name of the grouping
     * @param description The optional description of the user grouping.
     */
    public Grouping(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the grouping name
     * @return the grouping name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the grouping name
     * @param name the grouping name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the grouping description
     * @return the grouping description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the grouping description
     * @param description the grouping description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the user groups
     * @return the user groups
     */
    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    /**
     * Sets the user groups
     * @param userGroups the user groups
     */
    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    /**
     * Adds a trip to the grouping trips
     * @param trip the trip to add
     */
    public void addTrip(Node trip) {
        this.associatedTrips.add(trip);
    }

    /**
     * Removes a trip from the grouping associated trips
     * @param trip the trip to remove
     */
    public void removeTrip(Node trip) {
        this.associatedTrips.remove(trip);
    }

    /**
     * Get all trips associated with the grouping
     * @return the trips associated with the grouping
     */
    public List<Node> getTrips() {
        return associatedTrips;
    }

    /**
     * Get all users in the group
     * @return The users
     */
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            users.add(userGroup.getUser());
        }
        return users;
    }
}
