package unit.trips;

import models.Destination;
import org.junit.BeforeClass;
import org.junit.Test;
import play.mvc.Http;
import unit.common.BaseUnitWithUser;

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
}
