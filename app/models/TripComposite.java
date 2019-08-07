package models;

import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TripComposite extends TripNode {
    private String name;

    @OneToMany(cascade= CascadeType.ALL,orphanRemoval=true)
    private List<TripNode> tripNodes;

    public TripComposite(String name) {
        this.name = name;
        tripNodes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void add(TripNode node) {
        tripNodes.add(node);
    }

    public static Finder<Long, TripComposite> find = new Finder<>(TripComposite.class);

    public List<TripNode> getTripNodes() {
        return tripNodes;
    }
}
