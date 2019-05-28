package finders;

import controllers.constants.AdminConstants;
import io.ebean.Finder;
import models.Destination;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class DestinationFinder extends Finder<Long, Destination> {

    UserFinder userFinder = new UserFinder();


    public DestinationFinder() {
        super(Destination.class);
    }

    /**
     * retrieves destination from database by Id
     * @param id the id of the destination we search databse for
     * @return the found destination, otherwise null
     */
    public Destination findById(Long id) {
        return query().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

    /**
     * retrieves destination from database by Id
     * @param id the id of the destination we search databse for
     * @return the found destination, otherwise null
     */
    public Destination findByIdIncludeDeleted(Long id) {
        return query().setIncludeSoftDeletes().where().eq("id", id).findOneOrEmpty().orElse(null);
    }

    /**
     * retrieves all the destinations from the database that are available for user to see, based on if destination
     * is public or user created the destination
     * @param userId the Id of the user requesting to see destinations
     * @return a list of destinations that user is allowed to see
     */
    public List<Destination> getAvaliableDestinations(Long userId) {

        return query()
                .where()
                .and()
                .or()
                .eq("isPublic", true)
                .eq("user.id", userId)
                .endOr()
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


    /**
     * removes user attachment from destinaton when it is made public, admin is now the onlt
     * owner of the destination
     * @param destinationId
     */
    public void transferToAdmin(Long destinationId) {

        Destination destination = findById(destinationId);

        // Changes the owner to the global admin
        User globalAdminUser = userFinder.findById(AdminConstants.ADMIN_ID);
        destination.setUser(globalAdminUser);

        destination.update();

    }

}