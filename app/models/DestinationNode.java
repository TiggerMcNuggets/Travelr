package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DestinationNode extends Node {
    @ManyToOne
    private Destination destination;

    public DestinationNode(String name, User user, Destination destination) {
        super(name, user);
        this.destination = destination;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
