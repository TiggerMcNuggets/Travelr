package finders;

import io.ebean.Finder;
import models.Grouping;


public class GroupingFinder extends Finder<Long, Grouping> {

    public GroupingFinder(){
        super(Grouping.class);
    }

    /**
     * Retrieves user from database if user email matches email given
     * @param name
     * @return A grouping with the provided name
     */
    public Grouping findByName(String name) {
        return query().where().eq("name", name).findOne();
    }

    /**
     * Retrieves the Grouping element given id (includes soft-deletes)
     * @param id
     * @return the grouping with given id
     */
    public Grouping findByIdWithSoftDeletes(Long id) {
        return query().setIncludeSoftDeletes().where().eq("id", id).findOne();
    }

}
