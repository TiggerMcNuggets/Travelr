package models;

import finders.TripNodeFinder;
import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TripNode extends Node {

    @OneToMany(cascade= CascadeType.ALL,orphanRemoval=true, mappedBy = "parent")
    private List<Node> nodes;



    @Constraints.Required
    @OneToOne
    public Album defaultAlbum;

    public TripNode(String name, User user) {
        super(name, user);
        nodes = new ArrayList<>();
        this.defaultAlbum = new Album(user, name + " Album", true);
        this.defaultAlbum.insert();
    }

    public Album getDefaultAlbum() {
        return defaultAlbum;
    }

    public void setDefaultAlbum(Album defaultAlbum) {
        this.defaultAlbum = defaultAlbum;
    }

    public static TripNodeFinder find = new TripNodeFinder();

    public List<Node> getNodes() {
        return nodes;
    }
}