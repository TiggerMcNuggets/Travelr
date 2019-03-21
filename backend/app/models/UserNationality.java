package models;

import finders.UserNationalityFinder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class UserNationality extends BaseModel {

    public static final UserNationalityFinder find = new UserNationalityFinder();

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