package javaSteps.steps.userGroups;

import cucumber.api.java.en.Given;
import javaSteps.models.StateSingleton;
import models.Grouping;
import models.User;
import models.UserGroup;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PromoteGroupMember {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Sets the request for toggling the user's ownership status
     */
    @Given("I want to toggle promote the user")
    public void iWantToTogglePromoteTheUser() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s/member/%s/promote", state.getUser().getId(), state.getGroup().getId(), state.getGroupMember().getId()));
    }

    /**
     * Inserts a group owner into the group
     * @param dataTable The data table
     */
    @Given("the user group has the group owner")
    public void theUserGroupHasTheGroupOwner(List<Map<String, String>> dataTable) {
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

        UserGroup userGroup = new UserGroup(newGroupMember, group, true);
        userGroup.insert();
    }

    /**
     * Sets the group member in state to the user in state
     */
    @Given("I am the group member")
    public void iAmTheGroupMember() {
        this.state.setGroupMember(state.getUser());
    }
}
