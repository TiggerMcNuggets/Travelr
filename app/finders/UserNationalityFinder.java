package finders;

import io.ebean.Finder;
import models.UserNationality;
import models.User;
import models.Nationality;

public class UserNationalityFinder extends Finder<Long, UserNationality> {

    public UserNationalityFinder(){
        super(UserNationality.class);
    }

    /**
     * returns all user nationalities that match the user and nationality given
     * @param user
     * @param nationality
     * @return
     */
    public UserNationality ByUserNationality(User user, Nationality nationality) {
        if(user == null || nationality == null) {
            return null;
        }

        return query().where().eq("User", user).eq("Nationality", nationality).findOne();
    }
}
