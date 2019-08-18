package finders;

import io.ebean.Finder;
import models.Grouping;


public class GroupingFinder extends Finder<Long, Grouping> {

    public GroupingFinder(){
        super(Grouping.class);
    }

    /**
     * retrieves user from database if user email matches email given
     * @param name
     * @return A grouping with the provided name
     */
    public Grouping findByName(String name) {
        return query().where().eq("name", name).findOne();
    }

}
