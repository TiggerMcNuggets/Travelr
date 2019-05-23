package models;

import io.ebean.Finder;
import play.data.validation.Constraints;

import java.util.List;

public class DestinationEditRequest extends BaseModel {

    public static final Finder<Long, DestinationEditRequest> find = new Finder<>(DestinationEditRequest.class);

    @Constraints.Required
    public User user;

    @Constraints.Required
    public Destination destination;

    @Constraints.Required
    public List<TravellerType> travellerTypes;
}
