package models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import controllers.dto.Destination.CreateDestReq;
import controllers.dto.TravellerType.CreateTravellerTypeReq;
import io.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;
import finders.DestinationFinder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Destination entity managed by Ebean
 */
@Entity
public class Destination extends BaseModel {


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

    @NotNull
    @Constraints.Required
    public String district;

    @NotNull
    @Constraints.Required
    public String country;

    @Constraints.Required
    @OneToOne
    public Album defaultAlbum;

    @ManyToOne
    @JsonIgnore
    public User user;

    @ManyToMany(cascade= CascadeType.ALL)
    @JsonManagedReference
    public List<TravellerType> travellerTypes;

    @NotNull
    public boolean isPublic;

    public Destination(CreateDestReq request, User user) {
        this.name = request.name;
        this.latitude = request.latitude;
        this.longitude = request.longitude;
        this.type = request.type;
        this.district = request.district;
        this.country = request.country;
        this.user = user;
        this.isPublic = request.isPublic;
        this.defaultAlbum = new Album(user, name + " Album", true);
        this.defaultAlbum.insert();
    }

    // USED IN TEST DATA
    public Destination(String name, Double lat, Double lon, String type, String district, String country, User user) {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.type = type;
        this.district = district;
        this.country = country;
        this.user = user;
        this.isPublic = false;
        this.defaultAlbum = new Album(user, name + " Album", true);
        this.defaultAlbum.insert();
    }


    // GETTERS AND SETTERS


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<TravellerType> getTravellerTypes() {
        return travellerTypes;
    }

    public void setTravellerTypes(List<TravellerType> travellerTypes) {
        this.travellerTypes = travellerTypes;
    }

    public models.User getUser() {
        return user;
    }

    public void setUser(models.User user) {
        this.user = user;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public Album getDefaultAlbum() {
        return defaultAlbum;
    }

    public void setDefaultAlbum(Album defaultAlbum) {
        this.defaultAlbum = defaultAlbum;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}

