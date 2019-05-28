package repository;

import models.Nationality;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class NationalityRepository {

    private DatabaseExecutionContext context;

    @Inject
    public NationalityRepository(DatabaseExecutionContext context) {
        this.context = context;
    }

    /**
     * Gets a list of nationalities
     * @return completable future of list of nationalites
     */
    public CompletableFuture<List<Nationality>> getNationalities() {
        return supplyAsync(() -> Nationality.find.query().where().eq("is_old", false).findList(), context);
    }

    /**
     *
     * @param nationalities list of the names of the nationalities that need to be updated to being old
     * @return CompletableFuture<Integer> asynchronous value resolving in the number of updated rows
     */
    public CompletableFuture<Integer> updateNationalitiesIsOldParam(List<String> nationalities, boolean isOld) {
        return supplyAsync(() -> Nationality.db()
                .createSqlUpdate("UPDATE nationality SET is_old=(:isOld) where nationality.name in (:names)")
                .setParameter("isOld", isOld)
                .setParameter("names", nationalities)
                .execute());
    }
}
