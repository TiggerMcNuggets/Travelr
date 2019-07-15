package dto.trip;

import models.TripDestination;

import java.util.List;

public class TripDestinationDTO {

    public Long id;

    public String customName;

    public int ordinal;

    public int arrivalDate;

    public int departureDate;

    public DestinationDTO destination;

    public List<TripDestinationDTO> children;


}
