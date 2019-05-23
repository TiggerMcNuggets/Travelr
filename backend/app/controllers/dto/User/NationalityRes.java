package controllers.dto.user;

import models.UserNationality;

public class NationalityRes {
    private Long id;
    private String name;
    private boolean hasPassport;
    private boolean isOld;

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
