package models;

import finders.NodeFinder;
import io.ebean.Finder;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance
public abstract class Node extends BaseModel {

    private String name;

    private int arrivalDate;

    private int departureDate;

    private int ordinal;

    @ManyToOne
    private TripNode parent;

    @ManyToOne
    private User user;

    /**
     * The user group associated with the trip. Intentionally defaults to null.
     */
    @ManyToOne
    public Grouping userGroup;

//    /**
//     * The user statuses for trip attendance
//     */
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    public List<NodeUserStatus> userStatuses;


    public static final NodeFinder find = new NodeFinder();

    public Node(String name, User user) {
        this.name = name;
        this.user = user;
    }


    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TripNode getParent() {
        return parent;
    }

    public void setParent(TripNode parent) {
        this.parent = parent;
    }

    public User getUser() {
        return user;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(int arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public Grouping getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Grouping userGroup) {
        this.userGroup = userGroup;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * Adds user status
     * @param status the user status to add
     */
//    public void addStatus(NodeUserStatus status) {
//        this.userStatuses.add(status);
//    }
//
//    /**
//     * Removes user status
//     * @param status the user status to remove
//     */
//    public void removeStatus(NodeUserStatus status) {
//        this.userStatuses.remove(status);
//    }
//
//    /**
//     * Get all status associated with the trip
//     * @return the user statuses associated with the trip
//     */
//    public List<NodeUserStatus> getUserStatuses() {
//        return userStatuses;
//    }
}
