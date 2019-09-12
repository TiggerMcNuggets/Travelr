package dto.trip;

import dto.user.UserSimpleDTO;
import models.TripNode;

public class RootNodeDTO {

    public long id;
    public String name;
    public UserSimpleDTO user;
    public long albumId;

    public String groupName = "";


    public RootNodeDTO(TripNode node) {
        this.id = node.getId();
        this.name = node.getName();
        this.user = new UserSimpleDTO(node.getUser());
        this.albumId = node.getDefaultAlbum().getId();

        if (node.getUserGroup() != null) {
            this.groupName = node.getUserGroup().getName();
        }

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
