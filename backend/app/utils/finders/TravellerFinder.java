package utils.finders;

import io.ebean.Finder;
import models.Traveller;


public class TravellerFinder extends Finder<Long, Traveller> {

    /**
     * Construct using the default EbeanServer.
     */
    public TravellerFinder() {
        super(Traveller.class);
    }

}
