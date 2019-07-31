package controllers.dto.Destination;

import models.Album;
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

    public Long defaultAlbumId;
//
//    public String getName() {
//        return name;
//    }
//
//    public Double getLatitude() {
//        return latitude;
//    }
//
//    public Double getLongitude() {
//        return longitude;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public String getDistrict() {
//        return district;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public Long getOwnerId() {
//        return ownerId;
//    }
//
//    public boolean isPublic() {
//        return isPublic;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public List<TravellerType> getTravellerTypes() {
//        return travellerTypes;
//    }
//
//    public Album getAlbum() {
//        return album;
//    }

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
        this.defaultAlbumId = destination.getDefaultAlbum().getId();

    }

}
