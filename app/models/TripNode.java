package models;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;

@Entity
@Inheritance
public abstract class TripNode extends BaseModel {

    private String name;

    private int arrivalDate;

    private int departureDate;

    @ManyToOne
    private TripComposite parent;

    @ManyToOne
    private User user;

    public TripNode(String name, User user) {
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

    public TripComposite getParent() {
        return parent;
    }

    public void setParent(TripComposite parent) {
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
}
