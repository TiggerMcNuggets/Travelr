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
     * Creates a user group but does not insert it into the database so it will not exist for the user.
     */
    @Given("I do not own an user group")
    public void i_do_not_own_an_user_group() {
        Grouping group = new Grouping("Team 300", "The best team eva");
        group.setId(100L);
        state.setGroup(group);
    }

    /**
     * Creates a user group and adds you to it, but not as an owner.
     * @param dataTable The data to create a user group.
     */
    @Given("I do not own the user group")
    public void i_do_not_own_the_user_group(List<Map<String, String>> dataTable) {
        Map<String, String> groupInfo = dataTable.get(0);
        Grouping group = new Grouping(groupInfo.get("name"), groupInfo.get("description"));
        group.insert();
        state.setGroup(group);

        UserGroup userGroup = new UserGroup(state.getUser(), group, false);
        userGroup.insert();
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
     * Creates a group without inserting it to mock a 404 request
     * @param dataTable The details of the group to create
     */
    @Given("The group does not exist")
    public void the_group_does_not_exist(List<Map<String, String>> dataTable) {
        Map<String, String> groupInfo = dataTable.get(0);
        Grouping group = new Grouping(groupInfo.get("name"), groupInfo.get("description"));
        group.setId(100L);
        state.setGroup(group);
    }

    /**
     * Creates a user group for another user - same as above but expects state user to not be the authenticated user.
     * @param dataTable The data for the user group.
     */
    @Given("They own the user group")
    public void they_own_the_user_group(List<Map<String, String>> dataTable) {
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
