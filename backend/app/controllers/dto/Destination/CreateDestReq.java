package controllers.dto.Destination;

import play.data.validation.Constraints;

public class CreateDestReq {
    @Constraints.Required
    public String name;
    @Constraints.Required
    public Double latitude;
    @Constraints.Required
    public Double longitude;
    @Constraints.Required
    public String type;
    @Constraints.Required
    public String district;
    @Constraints.Required
    public String country;

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
}