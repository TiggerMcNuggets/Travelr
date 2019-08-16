package controllers.dto.UserGroup;

import controllers.dto.User.GetUserRes;
import models.Grouping;
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

    /**
     * Constructor given users, userGroup and owners (used for get single group)
     * @param users The list of users
     * @param userGroup The user group
     * @param owners The list of owners
     */
    public GetUserGroupRes(List<User> users, UserGroup userGroup, List<User> owners) {
        this.id = userGroup.getId();
        this.name = userGroup.getGrouping().getName();
        this.description = userGroup.getGrouping().getDescription();
        this.members = addUsers(users);
        this.owners = addOwners(owners);
    }

    /**
     * Constructor given grouping (used for get all groups that belong to a user)
     * @param grouping The group
     */
    public GetUserGroupRes(Grouping grouping) {

        this.id = grouping.getId();
        this.name = grouping.getName();
        this.description = grouping.getDescription();
        this.owners = new ArrayList<>();
        this.members = new ArrayList<>();

        for (UserGroup userGroup : grouping.getUserGroups()) {
            if (userGroup.isOwner()) {
                this.owners.add(userGroup.getUser().getId());
            }
            this.members.add(new GetUserRes(userGroup.getUser()));
        }
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
        for (User owner : owners) {
            Owners.add(owner.getId());
        }
        return Owners;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getOwners() {
        return owners;
    }

    public void setOwners(List<Long> owners) {
        this.owners = owners;
    }

    public List<GetUserRes> getMembers() {
        return members;
    }

    public void setMembers(List<GetUserRes> members) {
        this.members = members;
    }

    public static List<GetUserGroupRes> parseUserGroups(List<Grouping> groupings) {
        List<GetUserGroupRes> userGroupRes = new ArrayList<>();
        for (Grouping grouping : groupings) {
            userGroupRes.add(new GetUserGroupRes(grouping));
        }
        return userGroupRes;
    }
}
