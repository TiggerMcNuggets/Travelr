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
        System.out.println("####################HERE 2#######################");
        this.id = userGroup.getId();
        this.name = userGroup.getGroup().getName();
        this.description = userGroup.getGroup().getDescription();
        System.out.println("####################HERE 3#######################");
        this.members = addUsers(users);
        System.out.println("####################HERE 4#######################");
        this.owners = addOwners(owners);
    }


    public List<GetUserRes> addUsers(List<User> users) {
        List<GetUserRes> usersRes = new ArrayList<GetUserRes>();
        for (User user: users) {
            System.out.println(user.getFirstName());
            GetUserRes userRes = new GetUserRes(user);
//            System.out.println(userRes.getFirstName());
            System.out.println("########################################################");
            usersRes.add(userRes);
            System.out.println("########################################################");
        }
        return usersRes;
    }



    public List<Long> addOwners(List<User> owners) {
        List<Long> Owners = new ArrayList<Long>();
        for (User owner: owners) {
            Owners.add(owner.getId());
//            System.out.println("####################HERE 3#######################");
        }
        return Owners;
    }


}
