package dto.trip;

import dto.user.UserSimpleDTO;
import models.TripNode;

public class RootNodeDTO {

    public long id;
    public String name;
    public UserSimpleDTO user;


    public RootNodeDTO(TripNode node) {
        this.id = node.getId();
        this.name = node.getName();
        this.user = new UserSimpleDTO(node.getUser());
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

    public UserSimpleDTO getUser() {
        return user;
    }

    public void setUser(UserSimpleDTO user) {
        this.user = user;
    }
}
