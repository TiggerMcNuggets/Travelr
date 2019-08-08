package dto.trip;

import models.TripNode;

public class NavigationDTO {

    public long id;
    public String name;

    public NavigationDTO(TripNode node) {
        this.id = node.getId();
        this.name = node.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
