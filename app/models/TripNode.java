package models;

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

    public static Finder<Long, TripNode> find = new Finder<>(TripNode.class);

    public List<Node> getNodes() {
        return nodes;
    }
}
