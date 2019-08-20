package utils;


import cucumber.api.java.ca.Cal;
import models.Destination;
import models.Trip;
import models.TripDestination;
import models.User;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class iCalCreator {

    /**
     * Takes in any trip and returns a calendar object if there is at least one destination with arrival date
     * @param trip the trip that is getting turned into an iCal file
     * @return a Calendar object from the trip provided
     */
    public Calendar createCalendarFromTrip(Trip trip) {

        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId(trip.getId().toString()));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        int UID = 0;
        for (TripDestination destination: trip.destinations) {
            //need to change this part to cater for new trip set up, we only want the destinations
            //we want to get all the destinations that have this trip as parent
            //we can get this list from a new method in destinationNodeFinder

            if (destination.getArrivalDate() != 0 && destination.getDepartureDate() != 0) {
                //Addition of 24*60*60 will add one day to the date which in turn will make it export to .ics file with correct dates.
                Date start = new Date(new Date((destination.getArrivalDate() + 24*60*60) * 1000L));
                Date end = new Date(new Date((destination.getDepartureDate() + 24*60*60) * 1000L));

                VEvent dest = new VEvent(start, end, destination.getDestination().getName());
                dest.getProperties().add(new Uid(Integer.toString(UID)));

                calendar.getComponents().add(dest);
            } else if (destination.getArrivalDate() != 0 && destination.getDepartureDate() == 0) {
                Date start = new Date(new Date((destination.getArrivalDate() + 24*60*60) * 1000L));

                // Adding 2*(24*60*60) to force the event to start and end on the same day if there is no end date
                Date end = new Date(new Date((destination.getArrivalDate() + 2*(24*60*60)) * 1000L));

                VEvent dest = new VEvent(start, end, destination.getDestination().getName());
                dest.getProperties().add(new Uid(Integer.toString(UID)));

                calendar.getComponents().add(dest);
            }
            //Manually generating UID - unsure what these are but an error occurs if one is not provided.
            UID++;
        }

        calendar.validate();

        return calendar;
    }

}
