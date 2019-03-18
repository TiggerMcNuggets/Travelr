package models;

import play.data.validation.Constraints;
import finders.DestinationFinder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Destination entity managed by Ebean
 */
@Entity
public class Destination extends BaseModel {

    /**
     * Serializable class version number.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The destination finder used for querying destinations.
     */
    public static final DestinationFinder find = new DestinationFinder();

    /**
     * The name of the destination
     */
    @Column(unique=true)
    @Constraints.Required
    public String name;

    /**
     * The latitude value of the destination.
     */
    @NotNull
    @Constraints.Required
    public Double crd_latitude;

    /**
     * The longtitude coordinatte value of the destination.
     */
    @NotNull
    @Constraints.Required
    public Double crd_longitude;

    /**
     * The destination type.
     */
    @NotNull
    @Constraints.Required
    public String destination_type;

    /**
     * The distict where the destination is located.
     */
    public String district;

    /**
     * The country where the destination is located.
     */
    public String country;


    /**
     * The Destination constructor.
     * @param name The name of the destination
     * @param destination_type The type of the destination
     * @param district The distict where the destination is located.
     * @param lat The latitude coordinate value of the destination.
     * @param lng The longitude coordinate value of the destination.
     * @param country The country where the destination is located.
     */
    public Destination(String name, String destination_type, String district, Double lat, Double lng, String country) {
        this.name = name;
        this.crd_latitude = lat;
        this.crd_longitude = lng;
        this.destination_type = destination_type;
        this.district = district;
        this.country = country;
    }

    /**
     * Gets the name of the destination.
     * @return The name of the destination.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the destination.
     * @param name The name of the destination.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the latitude value of the destination.
     * @return The latitude value of the destination.
     */
    public Double getCrd_latitude() {
        return crd_latitude;
    }

    /**
     * Sets the latitude value of the destination.
     * @return The latitude value of the destination.
     */
    public void setCrd_latitude(Double crd_latitude) {
        this.crd_latitude = crd_latitude;
    }

    /**
     * Gets the longitude value of the destination.
     * @return The longitude value of the destination.
     */
    public Double getCrd_longitude() {
        return crd_longitude;
    }

    /**
     * Sets the longitude value of the destination.
     * @return The longitude value of the destination.
     */
    public void setCrd_longitude(Double crd_longitude) {
        this.crd_longitude = crd_longitude;
    }

    /**
     * Gets the type of the destination.
     * @return The type of the destination.
     */
    public String getDestination_type() {
        return destination_type;
    }


    /**
     * Sets the type of the destination.
     * @return The type of the destination.
     */
    public void setDestination_type(String destination_type) {
        this.destination_type = destination_type;
    }

    /**
     * Sets the district of the destination.
     * @return The district of the destination.
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Sets the district of the destination.
     * @return The district of the destination.
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Get the country where the destination is located.
     * @return The country where the destination is located.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country where the destination is located.
     * @param country The country where the destination is located.
     */
    public void setCountry(String country) {
        this.country = country;
    }

}

