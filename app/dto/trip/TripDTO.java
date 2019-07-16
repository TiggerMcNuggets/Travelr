package dto.trip;

import dto.user.UserSimpleDTO;

import java.util.List;

/**
 * Data transfer object used for the Trip Endpoints
 */
public class TripDTO {

    public long id;

    public String name;

    public String description;

    public boolean published;

    public UserSimpleDTO user;

    public List<TripDestinationDTO> destinations;

    //public List<TripDestinationDTO> unlinkedDestinations;



}
