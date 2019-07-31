package controllers.dto.Destination;

import models.Destination;
import models.TravellerType;

import java.util.List;

public class GetDestinationsRes {


    public String name;

    public Double latitude;

    public Double longitude;

    public String type;

    public String district;

    public String country;

    public Long ownerId;

    public boolean isPublic;

    public Long id;

    public List<TravellerType> travellerTypes;

    public Long albumId;


    public GetDestinationsRes(Destination destination) {
        this.name = destination.name;
        this.latitude = destination.latitude;
        this.longitude = destination.longitude;
        this.type = destination.type;
        this.district = destination.district;
        this.country = destination.country;
        this.isPublic = destination.isPublic;
        this.ownerId = destination.getUser().getId();
        this.id = destination.id;
        this.travellerTypes = destination.travellerTypes;
        this.albumId = destination.defaultAlbum.id;
    }

}
