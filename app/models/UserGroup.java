package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import finders.GroupingFinder;
import finders.UserGroupFinder;
import io.ebean.Finder;

import javax.persistence.*;

@Entity
public class UserGroup extends BaseModel {

    /**
     * The user grouping finder
     */
    public static final UserGroupFinder find = new UserGroupFinder();

    /**
     * The grouping member
     */
    @JsonIgnore
    @ManyToOne
    public User user;

    /**
     * The associated user grouping
     */
    @ManyToOne
    public Grouping grouping;

    /**
     * Whether the user is an owner or not
     */
    @Column(columnDefinition = "boolean not null default false")
    public boolean isOwner;


    /**
     * Basic constructor for a user grouping
     * @param user The new member of the user grouping
     * @param grouping The user grouping
     * @param isOwner if the member is an owner of the user grouping
     */
    public UserGroup(User user, Grouping grouping, boolean isOwner) {
        this.user = user;
        this.grouping = grouping;
        this.isOwner = isOwner;
    }

    /**
     * Gets the grouping user
     * @return the grouping user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the grouping user
     * @param user the grouping user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the user grouping
     * @return the user grouping
     */
    public Grouping getGrouping() {
        return grouping;
    }

    /**
     * Sets the user grouping to the grouping specified
     * @param grouping the user grouping
     */
    public void setGrouping(Grouping grouping) {
        this.grouping = grouping;
    }

    /**
     * Returns if the user is an owner of the grouping
     * @return if the user is an owner
     */
    public boolean isOwner() {
        return isOwner;
    }

    /**
     * Sets the user owner status
     * @param ownerPermissions the new owner permission
     */
    public void setOwner(boolean ownerPermissions) {
        isOwner = ownerPermissions;
    }
}
