package javaSteps.steps.traveller;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import javaSteps.models.StateSingleton;
import models.Nationality;
import models.TravellerType;
import models.User;
import models.UserNationality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains steps for testing getting a traveller
 */
public class GetTravellerSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @Given("The profile details are")
    public void myDetailsAre(List<Map<String, String>> dataTable) {
        Map<String, String> details = dataTable.get(0);
        User user = User.find.findById(state.getUser().getId());

        user.setFirstName(details.get("first"));
        user.setMiddleName(details.get("middle"));
        user.setLastName(details.get("last"));
        user.setDateOfBirth(Integer.valueOf(details.get("dob")));
        user.setGender(details.get("gender"));
        user.setEmail(details.get("email"));
        user.setAccountType(Integer.valueOf(details.get("accountType")));

        user.update();
        state.setUser(user);
    }

    @Given("The nationalities are")
    public void myNationalitiesAre(List<Map<String, String>> nationalities) {
        for (Map<String, String> nationality: nationalities) {
            Nationality nat = Nationality.find.byId(Long.valueOf(nationality.get("id")));
            UserNationality userNationality = new UserNationality(state.getUser(), nat, Boolean.valueOf(nationality.get("hasPassport")));
            userNationality.insert();
        }
    }

    @Given("The traveller types are")
    public void myTravellerTypesAre(List<Map<String, String>> travellerTypes) {
        User user = User.find.findById(state.getUser().getId());
        List<TravellerType> tTypes = new ArrayList<TravellerType>();
        for (Map<String, String> travellerType: travellerTypes) {
            TravellerType tType = TravellerType.find.byId(Long.valueOf(travellerType.get("travellerType")));
            tTypes.add(tType);
        }
        user.setTravellerTypes(tTypes);
        user.update();
    }

    @When("I want to get the profile")
    public void iWantToGetTheProfile() {
        state.getRequest().uri((String.format("http://localhost:9000/api/travellers/%s", state.getUser().getId())));
        state.getRequest().method("GET");
    }
}


