package controllers.dto.UserGroup;

import controllers.dto.User.GetUserRes;
import models.User;
import models.UserGroup;

import java.util.ArrayList;
import java.util.List;

public class GetUserGroupRes {

    public Long id;
    public String name;
    public String description;
    public List<Long> owners;
    public List<GetUserRes> members;

    public GetUserGroupRes(List<User> users, UserGroup userGroup, List<User> owners) {
        this.id = userGroup.getId();
        this.name = userGroup.getGroup().getName();
        this.description = userGroup.getGroup().getDescription();
        this.members = addUsers(users);
        this.owners = addOwners(owners);
    }

    /**
     * Goes through the list of users related to
     * the group and turns them into user res
     * @param users
     * @return List of GetUserRes's of all group members
     */
    public List<GetUserRes> addUsers(List<User> users) {
        List<GetUserRes> usersRes = new ArrayList<GetUserRes>();
        for (User user: users) {
            // Hacky work around to fix null pointer error
            user.getFirstName();
            GetUserRes userRes = new GetUserRes(user);
            usersRes.add(userRes);
        }
        return usersRes;
    }

    /**
     * Goes through all users of the group and adds
     * them to the owners array if they are an owner
     * @param owners
     * @return List of owner ID's
     */
    public List<Long> addOwners(List<User> owners) {
        List<Long> Owners = new ArrayList<Long>();
        for (User owner: owners) {
            Owners.add(owner.getId());
        }
        return Owners;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Long> getOwners() {
        return owners;
    }

    public List<GetUserRes> getMembers() {
        return members;
    }

}
