package models;

import finders.UserGroupFinder;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserGroup extends BaseModel {

    public static final UserGroupFinder find = new UserGroupFinder();

    /**
     * The name of the user group
     */
    @Column(length = 250)
    @Constraints.Required
    public String name;

    /**
     * The description of the user group
     */
    @Column(length = 300)
    public String description;


    /**
     * The members of the group
     */
    @ManyToMany(cascade= CascadeType.ALL)
    @JoinTable(name="user_group_members")
    public List<User> groupMembers;


    /**
     * The owners of the group.
     */
    @ManyToMany
    @JoinTable(name="user_group_owners")
    public List<User> owners;

    /**
     * The constructor for the user group
     * @param owner The initial owner of the group - its creator
     * @param name The name of the group
     * @param description The optional description of the user group.
     */
    public UserGroup(User owner, String name, String description) {
        this.groupMembers.add(owner);
        this.owners.add(owner);

        this.name = name;
        this.description = description;
    }

    /**
     * Adds a new member to the user group.
     * @param new_member The new member to add.
     */
    public void addMember(User new_member) {
        groupMembers.add(new_member);
    }


    /**
     * Removes a member from the user group.
     * @param member The group member to remove.
     */
    public void removeMember(User member) {
        groupMembers.remove(member);
    }

    /**
     * Returns a list of all the group members
     * @return the group members
     */
    public List<User> getGroupMembers() {
        return groupMembers;
    }

    /**
     * Promotes a group member to owner.
     * @param new_owner The group member to promote
     */
    public void addOwner(User new_owner) {
        owners.add(new_owner);
    }


    /**
     * Removes group members ownership
     * @param member The group member to demote.
     */
    public void removeOwner(User member) {
        owners.remove(member);
    }

    /**
     * Returns a list of all the owners of the group
     * @return a list of all the owners of the group
     */
    public List<User> getOwners() {
        return owners;
    }

    /**
     * Gets the group name
     * @return the group name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the group name
     * @param name the group name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the group description
     * @return the group description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the group description
     * @param description the group description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
