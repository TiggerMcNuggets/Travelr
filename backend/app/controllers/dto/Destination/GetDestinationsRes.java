package controllers.dto.destination;

import models.Destination;

public class GetDestinationsRes {


    public String name;

    public Double latitude;

    public Double longitude;

    public String type;

    public String district;

    public String country;

    public boolean isPublic;

    public Long id;

    public GetDestinationsRes(Destination destination) {
        this.name = destination.name;
        this.latitude = destination.latitude;
        this.longitude = destination.longitude;
        this.type = destination.type;
        this.district = destination.district;
        this.country = destination.country;
        this.isPublic = destination.isPublic;
        this.id = destination.id;
    }

}
