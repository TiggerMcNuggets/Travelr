package repository;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.ExpressionList;
import models.Traveller;
import models.TravellerType;
import play.db.ebean.EbeanConfig;
import play.mvc.Http;
import utils.Moment;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TravellerTypeRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public TravellerTypeRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Stores and saves the new types for a user
     * @param data json file including the request body parameters to create a traveller
     */
    private void fromJSONToTravellerType(JsonNode data, long id) {
        JsonNode tTypes = data.at("/types");
        ExpressionList<Traveller> travellerExpr = ebeanServer.find(Traveller.class).where().eq("id", id);
        if (travellerExpr.findCount() == 1) {
            Traveller traveller = travellerExpr.findOne();
            for (JsonNode node : tTypes) {
                String tType = node.at("/type").asText();
                // checking if an element with same t_type and traveller exists
                Integer typeCount =
                        ebeanServer.find(TravellerType.class)
                                .where().and().eq("traveller_id", id).ieq("t_type", tType).endAnd().findCount();
                if (typeCount == 0) {
                    TravellerType travellerType = new TravellerType(tType, traveller);
                    travellerType.save();
                }
            }
        }
    }

    /**
     * Inserts traveller in TravellerType table
     *
     * @param data request body
     * @return CompletionStage<TravellerType>
     */
    public CompletionStage<Traveller> add(JsonNode data, long id) {
        return supplyAsync(() -> {
            // if traveller does not exist, cannot insert
            Traveller traveller = ebeanServer.find(Traveller.class).where().eq("id", id).findOne();
            if (traveller == null) return null;
            fromJSONToTravellerType(data, id);
            return traveller;
        }, executionContext);
    }

    /**
     * Displays all travellerTypes meeting the (optional) request query parameters
     *
     * @return CompletionStage<List   <   TravellerType>>
     */
    public CompletionStage<List<TravellerType>> list(long id) {
        return supplyAsync(() -> {
            List<TravellerType> travellerTypes = ebeanServer.find(TravellerType.class).where().eq("traveller_id", id).findList();
            return travellerTypes;
        }, executionContext);
    }


    /**
     * Displays all destinations from a user meeting the (optional) request query parameters
     * @param id user id
     * @return @return CompletionStage<Integer> the number of deleted rows
     */
    public CompletionStage<Integer> delete(Long id, JsonNode data) {
        return supplyAsync(() -> {
            JsonNode traveller_types = data.at("/types");
            Traveller traveller = Traveller.find.byId(id);
            if (traveller == null) return null; // bad user
            Integer deletedRows = 0;
            for (JsonNode Ttype: traveller_types) {
                String type = Ttype.at("/type").asText();

                ExpressionList<TravellerType> travellerTypeExpr = ebeanServer.find(TravellerType.class)
                        .where()
                        .eq("traveller_id", id)
                        .and()
                        .eq("t_type", type);

                Integer typeCount = travellerTypeExpr.findCount();
                if (typeCount == 1) {
                    TravellerType travellerType = travellerTypeExpr.findOne();
                    if (travellerType != null) if (ebeanServer.delete(travellerType)) deletedRows++;
                }
            }
            return deletedRows;
        }, executionContext);
    }

}
