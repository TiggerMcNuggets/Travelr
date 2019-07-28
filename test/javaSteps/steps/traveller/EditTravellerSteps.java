package javaSteps.steps.traveller;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javaSteps.models.StateSingleton;
import models.Nationality;
import models.TravellerType;
import models.User;
import models.UserNationality;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

/**
 * Contains steps for testing editing a traveller
 */
public class EditTravellerSteps {

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @When("I want to edit the profile")
    public void iWantToEditTheProfile() {
        state.getRequest().uri((String.format("http://localhost:9000/api/travellers/%s", state.getUser().getId())));
        state.getRequest().method("PUT");
    }

    @Then("The traveller details are")
    public void theTravellerDetailsAre(List<Map<String, String>> dataTable) {
        User user = User.find.findById(state.getUser().getId());
        Map<String, String> userInfo = dataTable.get(0);

        Assert.assertEquals(userInfo.get("first"), user.getFirstName());
        Assert.assertEquals(userInfo.get("middle"), user.getFirstName());
        Assert.assertEquals(userInfo.get("last"), user.getFirstName());
        Assert.assertEquals(Integer.parseInt(userInfo.get("dob")), user.getDateOfBirth());
        Assert.assertEquals(userInfo.get("gender"), user.getGender());
    }

    @Then("The traveller's nationalities are")
    public void theTravellerSNationalitiesAre(List<Map<String, String>> nationalities) {
        User user = User.find.findById(state.getUser().getId());
        Assert.assertEquals(user.getNationalities().size(), nationalities.size());

        for (int i = 0; i < nationalities.size(); i++) {
            Map<String, String> nationality = nationalities.get(i);
            UserNationality userNationality = user.getNationalities().get(i);

            Assert.assertEquals(Long.valueOf(nationality.get("id")), userNationality.getNationality().getId());
            Assert.assertEquals(Boolean.valueOf(nationality.get("hasPassport")), userNationality.getHasPassport());
        }
    }

    @Then("The traveller's traveller types are")
    public void theTravellerSTravellerTypesAre(List<Map<String, String>> travellerTypes) {
        User user = User.find.findById(state.getUser().getId());
        Assert.assertEquals(user.getTravellerTypes().size(), travellerTypes.size());

        for (int i = 0; i < travellerTypes.size(); i++) {
            Map<String, String> travellerType = travellerTypes.get(i);
            TravellerType userTravellerType = user.getTravellerTypes().get(i);

            Assert.assertEquals(Long.valueOf(travellerType.get("travellerType")), userTravellerType.getId());
        }
    }
}
