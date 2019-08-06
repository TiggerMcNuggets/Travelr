package finders;

import io.ebean.Finder;
import models.UserGroup;

public class UserGroupFinder extends Finder<Long, UserGroup> {


    public UserGroupFinder() {
        super(UserGroup.class);
    }

    /**
     * Returns user group from the database if given id matches to an entry, otherwise returns null.
     * @param id The id of the user group to find
     * @return The user group object corresponding to the id
     */
    public UserGroup findGroupById(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

}