package models;

import io.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;
import finders.DestinationFinder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Destination entity managed by Ebean
 */
@Entity
public class Destination extends BaseModel {

    /**
     * The destination finder used for querying destinations.
     */
    public static final DestinationFinder find = new DestinationFinder();


    @Constraints.Required
    public String name;

    @NotNull
    @Constraints.Required
    public Double latitude;


    @NotNull
    @Constraints.Required
    public Double longitude;


    @NotNull
    @Constraints.Required
    public String type;

    public String district;

    public String country;

    @NotNull
    @ManyToOne
    public User user;


    // USED IN TEST DATA
    public Destination(String name, Double lat, Double lon, String type, String district, String country, User user) {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.type = type;
        this.district = district;
        this.country = country;
        this.user = user;
    }
}

