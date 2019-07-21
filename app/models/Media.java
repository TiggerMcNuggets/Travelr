package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@MappedSuperclass
@Entity
public class Media extends BaseModel{

    /**
     * is the media public or not.
     */
    @NotNull
    @Column(columnDefinition = "boolean default 0")
    public Boolean is_public = false;

    /**
     * The user which the media belongs to
     */
    @ManyToOne
    public User user;

    public Boolean getIs_public() {
        return is_public;
    }

    @Constraints.Required
    @ManyToMany(cascade= CascadeType.REMOVE)
    public List<Album> albums;

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
