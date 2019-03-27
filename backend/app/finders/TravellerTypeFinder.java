package finders;

import io.ebean.Finder;
import models.TravellerType;


public class TravellerTypeFinder extends Finder<Long, TravellerType> {

    /**
     * Construct using the default EbeanServer.
     */
    public TravellerTypeFinder() {
        super(TravellerType.class);
    }

}