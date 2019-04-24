package controllers.dto.Destination;

import io.ebean.annotation.JsonIgnore;
import models.User;
import play.data.validation.Constraints;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

public class GetDestinationsRes {


    public String name;

    public Double latitude;

    public Double longitude;

    public String type;

    public String district;

    public String country;

    public boolean isPublic;


    public
}
