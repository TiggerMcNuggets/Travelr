package controllers.dto.User;

import models.UserNationality;

public class NationalityRes {
    private Long id;
    private String name;
    private boolean hasPassport;

    public NationalityRes(UserNationality userNationality) {
        this.id = userNationality.nationality.id;
        this.name = userNationality.nationality.name;
        this.hasPassport = userNationality.hasPassport;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHasPassport() {
        return hasPassport;
    }
}
