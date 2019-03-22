package controllers.dto.User;

import play.data.validation.Constraints;
import java.util.List;

public class CreateUserReq {

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    public String middleName;

    @Constraints.Required
    public String gender;

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public int dateOfBirth;

    @Constraints.Required
    public List<NationalityReq> nationalities;

    @Constraints.Required
    public List<Integer> travellerTypes;

}
