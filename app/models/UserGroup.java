package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;

import javax.persistence.*;

@Entity
public class UserGroup extends BaseModel {

    /**
     * The user group finder
     */
    public static final Finder<Long, UserGroup> find = new Finder<>(UserGroup.class);

    /**
     * The group member
     */
    @JsonIgnore
    @ManyToOne
    public User user;

    /**
     * The associated user group
     */
    @ManyToOne
    public Group_ group;

    /**
     * Whether the user is an owner or not
     */
    @Column(columnDefinition = "boolean not null default false")
    public boolean isOwner;


    /**
     * Basic constructor for a user group
     * @param user The new member of the user group
     * @param group The user group
     * @param isOwner if the member is an owner of the user group
     */
    public UserGroup(User user, Group_ group, boolean isOwner) {
        this.user = user;
        this.group = group;
        this.isOwner = isOwner;
    }

    /**
     * Gets the group user
     * @return the group user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the group user
     * @param user the group user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the user group
     * @return the user group
     */
    public Group_ getGroup() {
        return group;
    }

    /**
     * Sets the user group to the group specified
     * @param group the user group
     */
    public void setGroup(Group_ group) {
        this.group = group;
    }

    /**
     * Returns if the user is an owner of the group
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
