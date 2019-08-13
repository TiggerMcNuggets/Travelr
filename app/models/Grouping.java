package models;

import finders.GroupingFinder;
import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.*;

@Entity
public class Grouping extends BaseModel {

    /**
     * The user group finder
     */
    public static final GroupingFinder find = new GroupingFinder();

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
     * The constructor for the user group
     * @param name The name of the group
     * @param description The optional description of the user group.
     */
    public Grouping(String name, String description) {
        this.name = name;
        this.description = description;
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
