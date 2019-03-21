package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import finders.UserFinder;
import io.ebean.Finder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class UserNationality extends BaseModel {

    public static final Finder<Long, UserNationality> find = new Finder<>(UserNationality.class);

    @JsonIgnore
    @ManyToOne
    public User user;

    @ManyToOne
    public Nationality nationality;

    @Column(columnDefinition = "boolean not null default false")
    public boolean hasPassport;

    public UserNationality(User user, Nationality nationality, boolean hasPassport) {
        this.user = user;
        this.nationality = nationality;
        this.hasPassport = hasPassport;
    }

}