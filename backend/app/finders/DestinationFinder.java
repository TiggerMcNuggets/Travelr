package finders;

import io.ebean.Finder;
import models.Destination;

import java.util.ArrayList;
import java.util.List;

public class DestinationFinder extends Finder<Long, Destination> {


    public DestinationFinder() {
        super(Destination.class);
    }

    public List<Destination> getUserDestinations(Long userId) {
        return query().where().eq("user.id", userId).findList();
    }

    public Destination findById(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

    public List<Destination> getAvaliableDestinations(Long userId) {

        return query()
                .where()
                .and()
                .or()
                .eq("isPublic", true)
                .eq("user.id", userId)
                .endOr()
                .eq("isDeleted", false)
                .endAnd()
                .findList();
    }

    /**
     *
     * @param destinationId The Id of the destination that is being made public
     * @return A list of destinations that are similar, realistically  this should be either a list of 1 or 0 destinations
     */
    public List<Destination> getSameDestinations(Long destinationId) {

        Destination destination = findById(destinationId);

        List<Destination> destinations = new ArrayList<>();


        // Get destinations where country, district and name are the same

        destinations.addAll(
                    query()
                    .where()
                    .and()
                    .eq("name", destination.name)
                    .eq("district", destination.district)
                    .eq("country", destination.country)
                    .endAnd()
                    .findList()
        );

        // Get destinations where latitude and longitude are the same

        destinations.addAll(
                query()
                .where()
                .and()
                .eq("longitude", destination.longitude)
                .eq("latitude", destination.latitude)
                .endAnd()
                .findList()
        );



        return destinations;

    }


    public void transferToAdmin(Long destinationId) {

        Destination destination = findById(destinationId);

        destination.user = null;

        destination.update();

    }

}