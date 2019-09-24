package finders;

import io.ebean.Finder;
import io.ebean.Query;
import models.File;

import java.util.List;

public class FileFinder extends Finder<Long, File> {

    public FileFinder() { super(File.class); }


    /*
        Returns a list of files for a TripNode
     */
    public List<File> getFilesForTripNode(Long tripId) {
        return query()
                .where()
                .eq("trip.id", tripId)
                .findList();
    }
}
