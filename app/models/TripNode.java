package models;

import finders.TripNodeFinder;
import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TripNode extends Node {

    @OneToMany(cascade= CascadeType.ALL,orphanRemoval=true, mappedBy = "parent")
    private List<Node> nodes;

    public TripNode(String name, User user) {
        super(name, user);
        nodes = new ArrayList<>();
    }

    public static TripNodeFinder find = new TripNodeFinder();

    public List<Node> getNodes() {
        return nodes;
    }
}
