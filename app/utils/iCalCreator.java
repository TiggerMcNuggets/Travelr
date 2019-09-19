package utils;


import models.*;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import java.util.HashMap;
import java.util.List;

public class iCalCreator {

    /**
     * Takes in any trip and returns a calendar object if there is at least one destination with arrival date
     * @param trip
     * @param destinations
     * @return a Calendar object from the trip provided
     */
    public Calendar createCalendarFromTripDestinations(TripNode trip, List<HashMap<TripNode, DestinationNode>> destinations) {

        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId(trip.getId().toString()));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        int UID = 0;

        for (HashMap destination: destinations) {
            DestinationNode node = (DestinationNode) destination.get(destination.keySet().toArray()[0]);

            if (node.getArrivalDate() != 0 && node.getDepartureDate() != 0) {
                //Addition of 24*60*60 will add one day to the date which in turn will make it export to .ics file with correct dates.
                Date start = new Date(new Date((node.getArrivalDate() + 24*60*60) * 1000L));
                Date end = new Date(new Date((node.getDepartureDate() + 24*60*60) * 1000L));

                VEvent dest = new VEvent(start, end, node.getDestination().getName());
                dest.getProperties().add(new Uid(Integer.toString(UID)));

                calendar.getComponents().add(dest);
            } else if (node.getArrivalDate() != 0 && node.getDepartureDate() == 0) {
                Date start = new Date(new Date((node.getArrivalDate() + 24*60*60) * 1000L));

                // Adding 2*(24*60*60) to force the event to start and end on the same day if there is no end date
                Date end = new Date(new Date((node.getArrivalDate() + 2*(24*60*60)) * 1000L));

                VEvent dest = new VEvent(start, end, node.getDestination().getName());
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
