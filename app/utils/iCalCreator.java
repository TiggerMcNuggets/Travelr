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
import net.fortuna.ical4j.model.property.Version;

import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class iCalCreator {

    public Calendar createCalendarFromTrip(Trip trip) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId(trip.getId().toString()));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        for (TripDestination destination: trip.destinations) {

            Date start = new Date(new Date(destination.getArrivalDate() * 1000L));
            Date end = new Date(new Date(destination.getDepartureDate() * 1000L));

            System.out.println(start);

            VEvent dest = new VEvent(start, end, destination.getDestination().getName());

            calendar.getComponents().add(dest);
        }
        //System.out.println(calendar.toString());
        //return calendar.toString();
        //calendar.validate();
        return calendar;
    }

}
