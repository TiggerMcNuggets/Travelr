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
}
