package models;

import finders.SlackUserFinder;
import io.ebean.annotation.NotNull;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SlackUser extends BaseModel  {

    public static final SlackUserFinder find = new SlackUserFinder();

    @NotNull
    @Constraints.Required
    @OneToOne
    public User user;

    @NotNull
    @Constraints.Required
    @Column(length = 300)
    public String accessToken;

    public List<String> slackChannelsOwned;

    public SlackUser(@Constraints.Required User user, @Constraints.Required String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.slackChannelsOwned = new ArrayList<String>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void addOwnedChannel(String channelName){
        this.slackChannelsOwned.add(channelName);
    }

    public void removeOwnedGroupName(String groupName){
        this.slackChannelsOwned.remove(groupName);
    }

    public List<String> getSlackChannelsOwned() {
        return slackChannelsOwned;
    }

    public void setSlackChannelsOwned(List<String> slackChannelsOwned) {
        this.slackChannelsOwned = slackChannelsOwned;
    }

}
