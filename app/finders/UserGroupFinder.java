package finders;

import io.ebean.Finder;
import models.Grouping;
import models.UserGroup;

import java.util.Optional;


public class UserGroupFinder extends Finder<Long, UserGroup> {

    public UserGroupFinder(){
        super(UserGroup.class);
    }

    /**
     * retrieves user from database if user email matches email given
     * @param userId
     * @return the UserGroupObject given the user_id of the user involved
     */
    public Optional<UserGroup> findByUserAndGroupId(Long userId, Long groupId) {
        return Optional.ofNullable(query()
                .where()
                    .eq("user_id", userId)
                    .eq("grouping_id", groupId)
                .findOne());
    }

}
