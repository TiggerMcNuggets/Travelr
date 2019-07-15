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
}
