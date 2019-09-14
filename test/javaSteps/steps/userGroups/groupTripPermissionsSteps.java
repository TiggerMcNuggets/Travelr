package javaSteps.steps.userGroups;

import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import javaSteps.models.StateSingleton;
import org.junit.Assert;
import repository.UserGroupRepository;
import service.TripService;


public class groupTripPermissionsSteps {

    private static UserGroupRepository userGroupRepository;
    private static TripService tripService;

    // Singleton object that holds shared values across steps
    private StateSingleton state = StateSingleton.getInstance();

    @Before(order=1)
    public void setup() {
        System.out.println();
        userGroupRepository = state.getApplication().injector().instanceOf(UserGroupRepository.class);
        tripService = state.getApplication().injector().instanceOf(TripService.class);
    }


    @Then("I am permitted to read for the group")
    public void iAmPermittedToReadForTheGroup() {
        Assert.assertTrue(userGroupRepository.isPermittedToRead(state.getGroup().getId(), state.getUser()).join());
    }

    @Then("I am not permitted to read for the group")
    public void iAmNotPermittedToReadForTheGroup() {
        Assert.assertFalse(userGroupRepository.isPermittedToRead(state.getGroup().getId(), state.getUser()).join());
    }

    @Then("I am permitted to write for the group")
    public void iAmPermittedToWriteForTheGroup() {
        Assert.assertTrue(userGroupRepository.isPermittedToWrite(state.getGroup().getId(), state.getUser()).join());
    }

    @Then("I am not permitted to write for the group")
    public void iAmNotPermittedToWriteForTheGroup() {
        Assert.assertFalse(userGroupRepository.isPermittedToWrite(state.getGroup().getId(), state.getUser()).join());
    }

    @Then("I am permitted to read for the trip")
    public void iAmPermittedToReadForTheTrip() {
        Assert.assertTrue(tripService.isPermittedToRead(state.getTrip(), state.getUser()).join());
    }

    @Then("I am not permitted to read for the trip")
    public void iAmNotPermittedToReadForTheTrip() {
        Assert.assertFalse(tripService.isPermittedToRead(state.getTrip(), state.getUser()).join());
    }

    @Then("I am permitted to write for the trip")
    public void iAmPermittedToWriteForTheTrip() {
        Assert.assertTrue(tripService.isPermittedToWrite(state.getTrip(), state.getUser()).join());
    }

    @Then("I am not permitted to write for the trip")
    public void iAmNotPermittedToWriteForTheTrip() {
        Assert.assertFalse(tripService.isPermittedToWrite(state.getTrip(), state.getUser()).join());
    }
}
