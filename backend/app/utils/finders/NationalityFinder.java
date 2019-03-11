package utils.finders;

import io.ebean.Finder;
import models.Nationality;
import models.Traveller;


public class NationalityFinder extends Finder<Long, Nationality> {

    /**
     * Construct using the default EbeanServer.
     */
    public NationalityFinder() {
        super(Nationality.class);
    }

}