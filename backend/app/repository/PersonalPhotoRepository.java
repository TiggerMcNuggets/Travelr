package repository;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.ExpressionList;
import io.ebean.Transaction;
import models.Nationality;
import models.PersonalPhoto;
import models.Traveller;
import play.db.ebean.EbeanConfig;
import play.db.ebean.Transactional;
import play.mvc.Http;
import utils.Moment;
import utils.comparators.TravellerComparatorForNationalitites;
import utils.comparators.TravellerComparatorForType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class PersonalPhotoRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;
    @Inject
    public PersonalPhotoRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }


    /**
     * Adds a new personal photo for a user.
     * @param id user id
     */
    public CompletionStage<Boolean> add(Long id, String imageFileName) {
        return supplyAsync(() -> {
            Traveller traveller = Traveller.find.byId(id);
            System.out.println(traveller);
            if (traveller == null) return false; // bad user
            PersonalPhoto photo = new PersonalPhoto(traveller, imageFileName);
            System.out.println(photo);
            photo.save();
            return true;
        }, executionContext);
    }

    /**
     * Returns all photos assoociated with a user.
     * @param id user id The user id
     * @return CompletionStage<List<PersonalPhoto>> The list of personal photos associated with the user.
     */
    public CompletionStage<List<PersonalPhoto>> list(Long id) {
        return supplyAsync(() -> {
            ExpressionList<PersonalPhoto> query = ebeanServer.find(PersonalPhoto.class).where().eq("traveller_id", id);
            return query.findList();
        }, executionContext);
    }

}


