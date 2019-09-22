package finders;

import io.ebean.Finder;
import models.SlackUser;

public class SlackUserFinder extends Finder<Long, SlackUser> {

    public SlackUserFinder(){
        super(SlackUser.class);
    }

    /**
     * retrieves user and its information from database if user id matches id given
     * @param id
     * @return
     */
    public SlackUser findByUserId(Long id) {
        return query().where().eq("user_id", id).findOneOrEmpty().orElse(null);
    }
}
