package controllers.dto.UserGroup;

import controllers.dto.User.GetUserRes;
import models.User;
import models.UserGroup;

import java.util.List;

public class GetUserGroupRes {

    public Long id;
    public String name;
    public String description;
    public List<Long> owners;
    public List<GetUserRes> members;

    public GetUserGroupRes(List<User> users, UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getGroup().getName();
        this.description = userGroup.getGroup().getDescription();
        this.members = addUsers(users);
    }
    
    
    public List<GetUserRes> addUsers(List<User> users) {
        List<GetUserRes> usersRes = null;
        for (User user: users) {
            usersRes.add(new GetUserRes(user));
        }
        return usersRes;
    }


}
