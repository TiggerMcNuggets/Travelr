package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Constraint;

@Entity
public class File extends BaseModel {

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String filepath;

    @ManyToOne
    public Node trip;

    @ManyToOne
    public User user;

}
