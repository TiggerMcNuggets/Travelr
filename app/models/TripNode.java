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

    @OneToMany(cascade= CascadeType.ALL,orphanRemoval=true)
    private List<Node> nodes;

    public TripNode(String name, User user) {
        super(name, user);
        nodes = new ArrayList<>();
    }

    public void add(Node node) {
        nodes.add(node);
    }

    public static TripNodeFinder find = new TripNodeFinder();

    public List<Node> getNodes() {
        return nodes;
    }
}
