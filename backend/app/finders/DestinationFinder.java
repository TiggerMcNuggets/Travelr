package finders;

import io.ebean.Finder;
import models.Destination;

import java.util.ArrayList;
import java.util.List;

public class DestinationFinder extends Finder<Long, Destination> {


    public DestinationFinder() {
        super(Destination.class);
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
                .eq("deleted", false)
                .endAnd()
                .findList();
    }

    /**
     * Gets same destinations by a given destination id
     * @param destinationId The Id of the destination that is being made public
     * @return A list of destinations that are similar, realistically  this should be either a list of 1 or 0 destinations
     */
    public List<Destination> getSameDestinations(Long destinationId) {

        Destination destination = findById(destinationId);
        return getSameDestinationsByDest(destination);

    }

    /**
     * Gets same destinations by a given destination
     * @param destination The destination that is being made public
     * @return A list of destinations that are similar, realistically  this should be either a list of 1 or 0 destinations
     */
    public List<Destination> getSameDestinationsByDest(Destination destination) {

        List<Destination> destinations = new ArrayList<>();


        // Get destinations where country, district and name are the same

        destinations.addAll(
                    query()
                    .where()
                    .and()
                    .eq("name", destination.name)
                    .eq("district", destination.district)
                    .eq("country", destination.country)
                    .eq("deleted", false)
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
                .eq("deleted", false)
                .endAnd()
                .findList()
        );

        return destinations;

    }

    /**
     * Get same destinations out of the ones avaiable to the user
     * @param destination The destination that is being made public
     * @param userId The id of the user
     * @return A list of destinations that are similar, realistically  this should be either a list of 1 or 0 destinations
     */
    public List<Destination> getSameDestinationsAvailable(Destination destination, Long userId) {

        List<Destination> destinations = getAvaliableDestinations(userId);
        List<Destination> sameDestinations = new ArrayList<Destination>();

        for (Destination dest : destinations) {

            if (!dest.deleted) {

                // Add if country, district and name are the same
                if (dest.getName().equals(destination.getName()) &&
                        dest.getDistrict().equals(destination.getDistrict()) &&
                        dest.getCountry().equals(destination.getCountry()) &&
                        !dest.deleted) {
                    sameDestinations.add(dest);
                }

                // Add if longitude and latitude are the same
                else if (dest.getLatitude().equals(destination.getLatitude()) && dest.getLongitude().equals(destination.getLongitude())) {
                    sameDestinations.add(dest);
                }
            }
        }

        return sameDestinations;
    }



    public void transferToAdmin(Long destinationId) {

        Destination destination = findById(destinationId);

        destination.setUser(null);

        destination.update();

    }

}