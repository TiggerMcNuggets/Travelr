package dto.trip;

import models.Destination;

public class DestinationDTO {

    public Long id;

    public String name;

    public Double latitude;

    public Double longitude;

    public String type;

    public String district;

    public String country;

    public String googleId;

    public DestinationDTO(Destination destination) {
        this.id = destination.getId();
        this.name = destination.getName();
        this.latitude = destination.getLatitude();
        this.longitude = destination.getLongitude();
        this.type = destination.getType();
        this.district = destination.getDistrict();
        this.country = destination.getCountry();
    }

    public DestinationDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
