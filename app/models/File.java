package models;

import finders.FileFinder;
import io.ebean.annotation.JsonIgnore;
import play.data.validation.Constraints;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
public class File extends BaseModel {

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String filepath;

    @Constraints.Required
    public String extension;

    @ManyToOne
    @JsonIgnore
    public Node trip;

    @ManyToOne
    @JsonIgnore
    public User user;

    public static final FileFinder find = new FileFinder();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Node getTrip() {
        return trip;
    }

    public void setTrip(Node trip) {
        this.trip = trip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static FileFinder getFind() {
        return find;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
