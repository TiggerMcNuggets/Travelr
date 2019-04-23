package models;

import controllers.DestinationController;
import controllers.dto.Destination.CreateDestReq;
import io.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;
import finders.DestinationFinder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Destination entity managed by Ebean
 */
@Entity
public class Destination extends BaseModel {

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

    public models.User getUser() {
        return user;
    }

    public void setUser(models.User user) {
        this.user = user;
    }

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

    @NotNull
    @ManyToOne
    @JsonIgnore
    public User user;

    public Destination(CreateDestReq request, User user) {
        this.name = request.name;
        this.latitude = request.latitude;
        this.longitude = request.longitude;
        this.type = request.type;
        this.district = request.district;
        this.country = request.country;
        this.user = user;
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
    }
}

