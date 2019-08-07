package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class TripDestinationLeaf extends TripNode {
    @ManyToOne
    private Destination destination;

    public TripDestinationLeaf(Destination destination) {
        this.destination = destination;
    }

    public String getName() {
        return destination.getName();
    }

    public Destination getDestination() {
        return destination;
    }
}
