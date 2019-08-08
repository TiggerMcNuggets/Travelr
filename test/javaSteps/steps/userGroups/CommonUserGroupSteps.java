package javaSteps.steps.userGroups;

import cucumber.api.java.en.Given;
import javaSteps.models.StateSingleton;
import models.*;

import java.util.List;
import java.util.Map;

public class CommonUserGroupSteps {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Creates a default group
     */
    @Given("A group exists")
    public void an_group_exists() {
        Grouping group = new Grouping("Team 300", "The best team eva");
        group.insert();
        state.setGroup(group);
    }

    /**
     * Creates a group with specified data and adds the signed in user as an owner
     * @param dataTable The details of the group to create
     */
    @Given("I own the user group")
    public void i_own_the_user_group(List<Map<String, String>> dataTable) {
        Map<String, String> groupInfo = dataTable.get(0);
        Grouping group = new Grouping(groupInfo.get("name"), groupInfo.get("description"));
        group.insert();
        state.setGroup(group);

        UserGroup userGroup = new UserGroup(state.getUser(), group, true);
        userGroup.insert();
    }

    /**
     * Adds another member to the group with specified data.
     * @param dataTable The user data
     */
    @Given("the user group has the group member")
    public void the_user_group_has_the_group_member(List<Map<String, String>> dataTable) {
        Map<String, String> userInfo = dataTable.get(0);

        User newGroupMember = new User(
                userInfo.get("first"),
                userInfo.get("last"),
                userInfo.get("email"),
                Integer.valueOf(userInfo.get("dob"))
        );
        newGroupMember.insert();
        state.setGroupMember(newGroupMember);
        Grouping group = state.getGroup();

        UserGroup userGroup = new UserGroup(newGroupMember, group, false);
        userGroup.insert();
    }

}
