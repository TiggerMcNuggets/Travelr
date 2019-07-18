package dto.user;

import models.User;

public class UserSimpleDTO {

    public String firstName;
    public String lastName;
    public long id;

    public UserSimpleDTO(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        id = user.getId();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
