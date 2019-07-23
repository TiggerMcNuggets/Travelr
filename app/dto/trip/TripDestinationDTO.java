package dto.trip;

import models.TripDestination;

import java.util.ArrayList;
import java.util.List;

public class TripDestinationDTO {

    public Long id;

    public int depth;

    public String customName;

    public int ordinal;

    public int arrivalDate;

    public int departureDate;

    public DestinationDTO destination;

    public TripDestinationDTO(TripDestination td) {
        this.id = td.getId();
        this.customName = td.getCustomName();
        this.ordinal = td.getOrdinal();
        this.arrivalDate = td.getArrivalDate();
        this.departureDate = td.getDepartureDate();
        this.destination = new DestinationDTO(td.getDestination());
        this.depth = td.getDepth();
    }

    public TripDestinationDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(int arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
    }

    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDestination(DestinationDTO destination) {
        this.destination = destination;
    }
}
