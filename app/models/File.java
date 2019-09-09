package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class File extends BaseModel {

    public String name;

    public String filepath;

    @ManyToOne
    public Node trip;

    @ManyToOne
    public User user;

}
