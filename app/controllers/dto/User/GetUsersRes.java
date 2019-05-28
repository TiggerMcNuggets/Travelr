package controllers.dto.User;

import models.User;

import java.util.ArrayList;
import java.util.List;

public class GetUsersRes {
    private List<GetUserRes> getUserRes;

    public GetUsersRes(List<User> users) {
        this.getUserRes = new ArrayList<GetUserRes>();
        for (User user : users) {
            this.getUserRes.add(new GetUserRes(user));
        }
    }

    public List<GetUserRes> getGetUserRes() {
        return getUserRes;
    }

}
