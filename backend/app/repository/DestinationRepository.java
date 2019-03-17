package repository;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Destination;
import play.db.ebean.EbeanConfig;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/** Destination Repository used for dealing with database interactions involving destinations data. */
public class DestinationRepository {

    /** The Ebean Server instance. */
    private final EbeanServer ebeanServer;

    /**
     * The execution context for executing database commands.
     */
    private final DatabaseExecutionContext executionContext;

    /**
     * The Constructor for the destination repository.
     * @param ebeanConfig The Ebean configuration
     * @param executionContext The database execution context
     */
    @Inject
    public DestinationRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Adds a destination to the database.
     * @param req The HTTP POST request.
     * @return The destination which was added.
     */
    public CompletionStage<Destination> add(Http.Request req) {
        return supplyAsync(() -> {
            JsonNode data = req.body().asJson();
            String name = data.at("/name").asText();
            String dest_type = data.at("/destination_type").asText();
            String district = data.at("/district").asText();
            Double lat = data.at("/crd_latitude").asDouble();
            Double lng = data.at("/crd_longitude").asDouble();
            String country = data.at("/country").asText();

            Destination destination = new Destination(name, dest_type, district, lat, lng, country);
            destination.save();
            return destination;
        }, executionContext);
    }

    /**
     * Deletes a destination from the database.
     * @param dest_id The id of the destination to be deleted.
     * @return The deleted destination.
     */
    public CompletionStage<Destination> delete(Long dest_id) {
        return supplyAsync(() -> {
            Destination dest = Destination.find.byId(dest_id);
            dest.delete();
            return dest;
        }, executionContext);
    }

    /**
     * Looks for a destination with dest_id as id
     * @param dest_id The id of the destination
     * @return The deleted destination as a JSON object
     */
    public CompletionStage<Destination> getOne(Long dest_id) {
        return supplyAsync(() -> {
            Destination dest = Destination.find.byId(dest_id);
            return dest;
        }, executionContext);
    }


    /**
     * Updates a destination based on data from a HTTP POST request.
     * @param req The HTTP Post Request.
     * @param dest_id The destination id to be deleted.
     * @return The updated destination object.
     */
    public CompletionStage<Destination> update(Http.Request req, Long dest_id) {
        return supplyAsync(() -> {
            Destination dest = Destination.find.byId(dest_id);

            JsonNode data = req.body().asJson();
            String name = data.at("/name").asText();
            String dest_type = data.at("/destination_type").asText();
            String district = data.at("/district").asText();
            Double lat = data.at("/crd_latitude").asDouble();
            Double lng = data.at("/crd_longitude").asDouble();
            String country = data.at("/country").asText();

            dest.setName(name);
            dest.setDestination_type(dest_type);
            dest.setDistrict(district);
            dest.setCrd_latitude(lat);
            dest.setCrd_longitude(lng);
            dest.setCountry(country);
            dest.save();



            return dest;
        }, executionContext);
    }

    /**
     * Selects all the destinations which are available in the database.
     * @return The list of destination objects from the database.
     */
    public CompletionStage<List<Destination>> list() {
        return supplyAsync(() -> ebeanServer.find(Destination.class).findList(), executionContext);
    }
}
