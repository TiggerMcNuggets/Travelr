package tasks;

import javax.inject.*;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import models.Nationality;
import play.inject.ApplicationLifecycle;
import play.Environment;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import play.libs.ws.*;
import play.mvc.Results;
import repository.NationalityRepository;
import scala.concurrent.ExecutionContext;
import utils.lists.StringListsUtils;


@Singleton
public class NationalitiesTask implements WSBodyReadables, WSBodyWritables {

    // constants
    private final String NATIONALITIES_API_URL = "https://restcountries.eu/rest/v2/all";
    private final int SCHEDULED_DELAY_SECONDS = 30;
    private final int SCHEDULED_REPETITION_SECONDS = 30;

    private WSClient ws;
    private ActorSystem actorSystem;
    private ExecutionContext executionContext;
    private NationalityRepository nationalityRepository;


    /**
     * Injects the application's Environment upon start-up and register hook(s) for shut-down.
     * Schedules the fetch of nationalities every given period of time
     */
    @Inject
    public NationalitiesTask(
            ApplicationLifecycle lifecycle,
            Environment environment,
            Config config,
            WSClient ws,
            ActorSystem actorSystem,
            ExecutionContext executionContext,
            NationalityRepository nationalityRepository
    ) {
        this.ws = ws;
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
        this.nationalityRepository = nationalityRepository;

        // scheduler to run on start and every given period
        this.actorSystem
                .scheduler()
                .schedule(
                        Duration.ofSeconds(SCHEDULED_DELAY_SECONDS), // delay
                        Duration.ofSeconds(SCHEDULED_REPETITION_SECONDS), // interval
                        () -> {
                            WSRequest request = ws.url(NATIONALITIES_API_URL);

                            request.get().thenComposeAsync(res -> {

                                // new nationalities
                                JsonNode node = res.getBody(json());
                                List<String> newNationalities = new ArrayList<>();
                                for (JsonNode singleNode : node) {
                                    newNationalities.add(singleNode.get("name").asText());
                                }

                                // ensures that if any nationality name stop existing, then starts existing again, this value will update accordingly
                                nationalityRepository.updateNationalitiesIsOldParam(newNationalities, false);

                                // existing nationalities
                                List<Nationality> existingNationalities = Nationality.find.all();
                                List<String> existingNationalitiesNames = new ArrayList<>();
                                for (Nationality nat: existingNationalities) {
                                    existingNationalitiesNames.add(nat.name);
                                }

                                // list of nationalities to make old
                                List<String> nationalitiesToMakeOld = StringListsUtils.subtraction(
                                        existingNationalitiesNames,
                                        newNationalities
                                );

                                // actual update
                                nationalityRepository.updateNationalitiesIsOldParam(nationalitiesToMakeOld, true);


                                // list of nationalities to save to db
                                List<String> nationalitiesToInsert = StringListsUtils.subtraction(
                                        newNationalities,
                                        existingNationalitiesNames
                                );

                                // actual insertion
                                for (String natName : nationalitiesToInsert) {
                                    Nationality nat = new Nationality(natName);
                                    nat.save();
                                }

                                return CompletableFuture.completedFuture(Results.ok("ok"));
                            });
                        },
                        this.executionContext
                );


        // Shut-down hook
        lifecycle.addStopHook( () -> CompletableFuture.completedFuture(null));
    }

}

