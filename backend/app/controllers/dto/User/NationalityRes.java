package controllers.dto.User;

import models.UserNationality;

/**
 * This class creates the response to return when a list of nationalities is requested for users
 */
public class NationalityRes {
    private Long id;
    private String name;
    private boolean hasPassport;
    private boolean isOld;

    /**
     *
     * @param userNationality UserNationality object, it is an object storing the nationality infos about a user (has passport for nationality,
     *                        is the nationality not existing anymore[isOld], its name and id)
     */
    public NationalityRes(UserNationality userNationality) {
        this.id = userNationality.nationality.id;
        this.isOld = userNationality.nationality.is_old;
        this.name = userNationality.nationality.name;
        this.hasPassport = userNationality.hasPassport;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOld() {
        return isOld;
    }

    public boolean isHasPassport() {
        return hasPassport;
    }
}
