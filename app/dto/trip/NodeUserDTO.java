package dto.trip;

import models.Node;
import models.NodeUserStatus;
import models.UserGroup;

public class NodeUserDTO {

    public Long userId;

    public String firstName;

    public String lastName;

    public String status;

    public boolean owner;

    public NodeUserDTO(UserGroup usergroup, Node node) {
        this.userId = usergroup.getUser().getId();
        this.firstName = usergroup.getUser().getFirstName();
        this.lastName = usergroup.getUser().getLastName();
        this.status = this.findStatus(usergroup, node);
        this.owner = usergroup.isOwner();
    }

    public String findStatus(UserGroup usergroup, Node node) {
        NodeUserStatus userStatus = NodeUserStatus.find.query().where().eq("user", usergroup.getUser()).eq("node", node).findOne();
        if (userStatus == null) {
            return "NOT GOING";
        } else {
            return userStatus.getTripStatus().toString();
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
