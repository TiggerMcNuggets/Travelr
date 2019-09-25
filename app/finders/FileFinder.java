package finders;

import io.ebean.Finder;
import io.ebean.Query;
import models.File;

import java.util.List;
import java.util.Optional;

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

    /*
        Returns a file by id including the deleted files
     */
    public Optional<File> getFileByIdIncludeDeleted(Long fileId){
        return query()
                .setIncludeSoftDeletes()
                .where()
                .eq("id", fileId)
                .findOneOrEmpty();
    }
}
