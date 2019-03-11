package repository;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.ExpressionList;
import models.Nationality;
import models.Traveller;
import play.db.ebean.EbeanConfig;
import play.db.ebean.Transactional;

import javax.inject.Inject;
import javax.xml.transform.Result;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class NationalityRepository {
    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public NationalityRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * Displays all destinations from a user meeting the (optional) request query parameters
     * @param id user id
     * @return CompletionStage<Integer> the number of inserted rows
     */
    @Transactional
    public CompletionStage<Integer> add(Long id, JsonNode data) {
        return supplyAsync(() -> {
            JsonNode nationalities = data.at("/nationalities");
            Traveller traveller = Traveller.find.byId(id);
            if (traveller == null) return null; // bad user
            Integer insertedRows = 0;
            for (JsonNode nationality: nationalities) {
                String country = nationality.at("/country").asText();
                Boolean hasPassport = nationality.at("/has_passport").asBoolean();
                Integer hasPassportToInt = 0;
                if (hasPassport) hasPassportToInt = 1;
                // TODO: fix primary key problem, after that this part will not be required anymore
                Integer natCount =
                        ebeanServer.find(Nationality.class)
                                .where()
                                .eq("nationality", country)
                                .and()
                                .eq("traveller_id", id)
                                .findCount();
                if (natCount == 0) {
                    Nationality travellerNationality = new Nationality(traveller, country, hasPassportToInt);
                    travellerNationality.save();
                    insertedRows++;
                }
            }
            return insertedRows;
        }, executionContext);
    }

    /**
     * Displays all destinations from a user meeting the (optional) request query parameters
     * @param id user id
     * @return @return CompletionStage<Integer> the number of deleted rows
     */
    public CompletionStage<Integer> delete(Long id, JsonNode data) {
        return supplyAsync(() -> {
            JsonNode nationalities = data.at("/nationalities");
            Traveller traveller = Traveller.find.byId(id);
            if (traveller == null) return null; // bad user
            Integer deletedRows = 0;
            for (JsonNode nationality: nationalities) {
                String country = nationality.at("/country").asText();

                ExpressionList<Nationality> travellerNationalityExpr = ebeanServer.find(Nationality.class)
                        .where()
                        .eq("traveller_id", id)
                        .and()
                        .eq("nationality", country);

                Integer natCount = travellerNationalityExpr.findCount();
                if (natCount == 1) {
                    Nationality travellerNationality = travellerNationalityExpr.findOne();
                    if (travellerNationality != null) if (ebeanServer.delete(travellerNationality)) deletedRows++;
                }
            }
            return deletedRows;
        }, executionContext);
    }

    /**
     * Displays all destinations from a user meeting the (optional) request query parameters
     * @param id user id
     * @return CompletionStage<List<Nationality>>
     */
    public CompletionStage<List<Nationality>> list(Long id) {
        return supplyAsync(() -> {
            ExpressionList<Nationality> query = ebeanServer.find(Nationality.class).where().eq("traveller_id", id);
            return query.findList();
        }, executionContext);
    }

}
