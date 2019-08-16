package javaSteps.steps.userGroups;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.UserGroup;
import org.junit.Assert;

import java.util.Optional;

public class PromoteGroupMember {

    // Singleton object that holds shared values across features
    private StateSingleton state = StateSingleton.getInstance();

    @Given("The user is now an not a owner of the group")
    public void the_user_is_now_an_not_a_owner_of_the_group() {
        Optional<UserGroup> userGroup = findUserGroup();
        Assert.assertTrue(userGroup.isPresent());
        Assert.assertFalse(userGroup.get().isOwner);
    }

    @Given("I want to promote the user to group owner")
    public void i_want_to_promote_the_user_to_group_owner() {
        state.getRequest().method("PUT");
        state.getRequest().uri(String.format("https://localhost:9000/api/users/%s/group/%s/member/%s/promote", state.getUser().getId(), state.getGroup().getId(), state.getGroupMember().getId()));
    }

    /**
     * Finds user group by user and group objects
     * @return
     */
    private Optional<UserGroup> findUserGroup() {
        return UserGroup.find.findByUserId(state.getGroupMember().getId(), state.getGroup().getId());
    }

}
