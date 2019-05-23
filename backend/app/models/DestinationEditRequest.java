package models;

import play.data.validation.Constraints;

import java.util.List;

public class DestinationEditRequest extends BaseModel {


    @Constraints.Required
    public User user;

    @Constraints.Required
    public Destination destination;


    @Constraints.Required
    public List<TravellerType> travellerTypes;




}
