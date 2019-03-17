package repository;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.*;
import models.Nationality;
import models.Traveller;
import models.TravellerType;
import play.db.ebean.EbeanConfig;
import play.db.ebean.Transactional;
import play.mvc.Http;
import utils.Moment;
import utils.comparators.NationalityComparator;
import utils.comparators.TravellerComparatorForNationalitites;
import utils.comparators.TravellerComparatorForType;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TravellerRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    private final NationalityRepository nationalityRepository;
    private final TravellerTypeRepository travellerTypeRepository;

    @Inject
    public TravellerRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
        this.nationalityRepository = new NationalityRepository(ebeanConfig, this.executionContext);
        this.travellerTypeRepository = new TravellerTypeRepository(ebeanConfig, this.executionContext);
    }

    /**
     * Inserts traveller in Traveller table
     *
     * @param req http request
     * @return CompletionStage<Traveller>
     */
    public CompletionStage<Traveller> add(Http.Request req) {
        return supplyAsync(() -> {
            try (Transaction transaction = ebeanServer.beginTransaction()) {
                JsonNode data = req.body().asJson();
                Traveller traveller = fromJSONToTraveller(data);
                if (traveller == null) {
                    System.out.println("Roll back due to traveller insertion");
                    transaction.rollback();
                    return null;
                }
                traveller.save();
                this.nationalityRepository.add(traveller.getId(), data);
                this.travellerTypeRepository.add(data, traveller.getId());
                transaction.commit();
                return traveller;
                }}, executionContext);
    }

    /**
     * Displays all travellers meeting the (optional) request query parameters
     *
     * @return CompletionStage<List<Traveller>>
     */
    @Transactional
    public CompletionStage<List<Traveller>> list(String fname, String lname, String gender, Integer minAge, Integer maxAge, List<String> nationalities, List<String> traveller_types, String orderBy) {
        return supplyAsync(() -> {
            // TODO: GITLAB-ISSUE #2 make search for nationalities and types case insensitive
            ExpressionList<Traveller> query = ebeanServer.find(Traveller.class).fetch("nationalities", "nationality").fetch("types", "tType").where().and();
            if (fname != null) query.ieq("fname", fname);
            if (lname != null) query.ieq("lname", lname);
            if (gender != null) query.ieq("gender", gender);
            if (nationalities != null) query.in("nationalities.nationality", nationalities);
            if (traveller_types != null) query.in("types.tType", traveller_types);
            if (orderBy != null && !orderBy.toLowerCase().equals("type") && !orderBy.toLowerCase().equals("nationality")) {
                String order = sanitiseOrderBy(orderBy);
                query.setOrderBy(order);
            }
            // if confused by next line, check moment findDOBFromAge documentation, shows wht I am passign maxAge as first param and minAge as second
            query.raw("dob BETWEEN ? AND ?", Moment.findDOBFromAge(maxAge), Moment.findDOBFromAge(minAge));
            query.endAnd();
            List<Traveller> travellers = query.findList();
            if (orderBy != null) {
                if (orderBy.equals("nationality")) {
                    Collections.sort(travellers, new TravellerComparatorForNationalitites());
                } else if (orderBy.equals("type")) {
                    Collections.sort(travellers, new TravellerComparatorForType());
                }
            }
            return travellers;
        }, executionContext);
    }


    /**
     * Finds a traveller with the specified id.
     * @param id The id of the traveller to find.
     * @return A list containing the traveller with the specified id.
     */
    @Transactional
    public CompletionStage<List<Traveller>> profile(Long id) {
        return supplyAsync(() -> {
            List<Traveller> travellers = new ArrayList<Traveller>();
            Traveller traveller = Traveller.find.byId(id);
            travellers.add(traveller);
            return travellers;
        }, executionContext);
    }

    /**
     *
     * @param data the json object containing the Http.Request body parameters
     * @param id user id
     * @return boolean representing success or failure in updating the traveller
     */
    public CompletionStage<Boolean> update(JsonNode data, Long id) {
        return supplyAsync(() -> {
            try (Transaction transaction = ebeanServer.beginTransaction()) {
                Traveller traveller = updateTraveller(Traveller.find.byId(id), data);
                if (traveller == null) {
                    transaction.rollback();
                    return false;
                } else {
                    traveller.save();
                }
                transaction.commit();
                return true;
            }
        });
    }

    /**
     *
     * @param id user id
     * @return boolean representing success or failure in deleting the traveller
     */
    public CompletionStage<Boolean> delete(Long id) {
        return supplyAsync(() -> {
            try (Transaction transaction = ebeanServer.beginTransaction()) {
                ebeanServer.find(Nationality.class).where().eq("traveller_id", id).delete();

                Traveller traveller = ebeanServer.find(Traveller.class).where().eq("id", id).findOne(); // sure there exists only one as it is primary key
                if (traveller == null) return false;
                boolean isTravellerDeleted = traveller.delete();
                transaction.commit();
                return isTravellerDeleted;
            }
        });
    }

    /**
     * Helper method to deal with the Traveller parameters updates
     *
     * @param traveller
     * @param data json object containing the Http.Request body
     * @return the updated traveller instance
     */
    private Traveller updateTraveller(Traveller traveller, JsonNode data) {
        String travellerFName = data.at("/fname").asText();
        String travellerMName = data.at("/mname").asText();
        String travellerLName = data.at("/lname").asText();
        Date dob = (new Moment(data.at("/dob").asText())).toDOBFormat();
        if (dob == null) {
            return null;
        }
        String gender = data.at("/dob").asText();
        String password = data.at("/password").asText();

        traveller.setFname(travellerFName);
        traveller.setMname(travellerMName);
        traveller.setLname(travellerLName);
        traveller.setDob(dob);
        traveller.setGender(gender);
        traveller.setPassword(password);
        return traveller;
    }

    /**
     * Helper method to insert a new Traveller object in the db
     *
     * @param data json file including the request body parameters to create a traveller
     * @return a Traveller object
     */
    private Traveller fromJSONToTraveller(JsonNode data) {
        String travellerFName = data.at("/fname").asText();
        String travellerMName = data.at("/mname").asText();
        String travellerLName = data.at("/lname").asText();
        Date dob = (new Moment(data.at("/dob").asText())).toDOBFormat();
        if (dob == null) {
            return null;
        }
        String gender = data.at("/gender").asText();
        String email = data.at("/email").asText();
        String password = data.at("/password").asText();
        Date timestamp = new Date();
        Integer travellerCount = ebeanServer.find(Traveller.class).where().ieq("email", email).findCount();
        if (travellerCount != 0) return null;
        Traveller traveller = new Traveller(travellerFName, travellerMName, travellerLName, dob, gender, email, password, timestamp);
        return traveller;
    }

    /**
     * Method which checks the users credentials against what is in the database.
     *
     * @param data json object containing the Http.Request body which contains the credentials.
     * @return The traveller if it was found in the database, null otherwise.
     */
    public CompletionStage<Traveller>  login(JsonNode data) {

        return supplyAsync(() -> {
            String travellerEmail = data.at("/email").asText();
            String travellerPassword = data.at("/password").asText();

            Traveller traveller = Traveller.find.query().where()
                    .eq("email", travellerEmail)
                    .eq("password", travellerPassword)
                    .findOne();

            try {
                return traveller;
            } catch (NullPointerException e) {
                return null;
            }
        });
    }




    private String sanitiseOrderBy(String orderBy) {
        //TODO: need to understand how I can sort this for
        boolean isValidOrderBy = orderBy.toLowerCase().equals("fname") ||
                        orderBy.toLowerCase().equals("mname") ||
                        orderBy.toLowerCase().equals("lname") ||
                        orderBy.toLowerCase().equals("dob") ||
                        orderBy.toLowerCase().equals("age") ||
                        orderBy.toLowerCase().equals("nationality");
        if (isValidOrderBy) {
            if (orderBy.equals("dob")) {
                return  orderBy + " desc";
            }
            return orderBy.toLowerCase();
        }
        return null;
    }
}


