package dto.trip;

import models.DestinationNode;
import models.Node;
import models.TripNode;

public class NodeDTO {
    public Long id;

    public String name;

    public int arrivalDate;

    public int departureDate;

    public int ordinal;

    public DestinationDTO destination;

    public String type;

    public NodeDTO(Node node) {
        this.id = node.getId();
        this.name = node.getName();
        this.arrivalDate = node.getArrivalDate();
        this.departureDate = node.getDepartureDate();
        this.ordinal = node.getOrdinal();

        if (node.getClass() == TripNode.class) {
            this.type = "trip";
        } else {
            this.type = "destination";
            this.destination = new DestinationDTO(((DestinationNode)node).getDestination());
        }
    }
}
