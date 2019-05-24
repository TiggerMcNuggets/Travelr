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
        return supplyAsync(() -> {
            return Nationality.find.query().where().eq("is_old", false).findList();
        }, context);
    }
}
