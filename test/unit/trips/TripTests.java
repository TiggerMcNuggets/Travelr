package unit.trips;

import dto.trip.TripDTO;
import models.Destination;
import models.Trip;
import models.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.mvc.Http;
import service.TripService;
import unit.common.BaseUnit;
import unit.common.BaseUnitWithUser;

import javax.inject.Inject;

import static org.junit.Assert.*;

public class TripTests extends BaseUnitWithUser {

    static Destination dest1;
    static Destination dest2;
    static Destination dest3;

    @BeforeClass
    public static void setupTripTests() {

        dest1 = new Destination("Dest 1", 5.0, 5.0, "River", "Canterbury", "New Zealand", user);
        dest1.insert();
        dest2 = new Destination("Dest 2", 5.0, 5.0, "River", "Canterbury", "New Zealand", user);
        dest2.insert();
        dest3 = new Destination("Dest 3", 5.0, 5.0, "River", "Canterbury", "New Zealand", user);
        dest3.insert();
    }

    @Test
    public void testCreateTrip() {
        Trip trip = new Trip("My Trip", "My Trip Description", user);

        trip.insert();

        assertEquals("My Trip Description", trip.getDescription());
        assertEquals("My Trip", trip.getName());
        assertEquals(trip.user.getId(), user.getId());
    }

    @Test
    public void testToggleTripDeletion() {
        Trip trip = new Trip("My Trip", "My Trip Description", user);
        trip.insert();

        Http.RequestBuilder requestBuilder = new Http.RequestBuilder();

        requestBuilder.uri((String.format("http://localhost:9000/api/users/%s/trips/%s/toggle_deleted", user.getId(), trip.getId())));

    }
}
