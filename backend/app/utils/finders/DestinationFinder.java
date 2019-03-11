package utils.finders;

import io.ebean.Finder;
import models.Destination;

public class DestinationFinder extends Finder<Long, Destination> {

    /**
     * Construct using the default EbeanServer.
     */
    public DestinationFinder() {
        super(Destination.class);
    }

}