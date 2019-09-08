package dto.trip;

import models.*;

import java.util.List;

public class NodeDTO {
    public Long id;

    public String name;

    public int arrivalDate;

    public int departureDate;

    public int ordinal;

    public DestinationDTO destination;

    public String type;

    public List<NodeUserDTO> usergroup;

    public NodeDTO() {

    }

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

        Grouping nodeUsergroup = node.getUserGroup();
        if (nodeUsergroup != null)
            findUsergroups(nodeUsergroup, node);
    }


    public void findUsergroups(Grouping grouping, Node node) {
        for (UserGroup user : grouping.getUserGroups()) {
            this.usergroup.add(new NodeUserDTO(user, node));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public DestinationDTO getDestination() {
        return destination;
    }

    public void setDestination(DestinationDTO destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NodeUserDTO> getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(List<NodeUserDTO> usergroup) {
        this.usergroup = usergroup;
    }


}
