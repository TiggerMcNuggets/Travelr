package models;

import controllers.dto.destination.CreateDestReq;
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

    @ManyToOne
    @JsonIgnore
    public User user;

    @ManyToMany(cascade= CascadeType.ALL)
    public List<TravellerType> travellerTypes;

    @NotNull
    public boolean isPublic;


    @NotNull
    @JsonIgnore
    @Column(columnDefinition = "boolean default 0")
    public boolean deleted;

    public Destination(CreateDestReq request, User user) {
        this.name = request.name;
        this.latitude = request.latitude;
        this.longitude = request.longitude;
        this.type = request.type;
        this.district = request.district;
        this.country = request.country;
        this.user = user;
        this.isPublic = request.isPublic;
        this.travellerTypes = retrieveTravellerTypes(request.travellerTypes);
        this.deleted = false;
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
        this.deleted = false;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * attaches numbers to actual traveller types from list when creating traveller
     * types for new destination
     * @param requestTravellerTypes list of numbers that reference traveller types from request
     * @return list of actual traveller types for destination
     */
    public ArrayList<TravellerType> retrieveTravellerTypes(List<Integer> requestTravellerTypes) {
        ArrayList<TravellerType> travellerTypes = new ArrayList<TravellerType>();
        if (requestTravellerTypes.size() > 0) {
            for (long i : requestTravellerTypes) {
                travellerTypes.add(TravellerType.find.byId(i));
            }
        }
        return travellerTypes;
    }
}

