package tasks;

import javax.inject.*;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import models.Nationality;
import play.inject.ApplicationLifecycle;
import play.Environment;

import java.time.Duration;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import play.libs.ws.*;
import play.mvc.Results;
import scala.concurrent.ExecutionContext;


@Singleton
public class NationalitiesTask implements WSBodyReadables, WSBodyWritables {

    private WSClient ws;
    private ActorSystem actorSystem;
    private ExecutionContext executionContext;
    private final String NATIONALITIES_API_URL = "https://restcountries.eu/rest/v2/all";
    private final int SCHEDULED_DELAY = 60;
    private final int SCHEDULED_REPETITION = 60;

    /**
     * Inject the application's Environment upon start-up and register hook(s) for shut-down.
     * Schedules the fetch of nationalities every given period of time
     */
    @Inject
    public NationalitiesTask(
            ApplicationLifecycle lifecycle,
            Environment environment,
            Config config,
            WSClient ws,
            ActorSystem actorSystem,
            ExecutionContext executionContext
    ) {
        this.ws = ws;
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        // scheduler to run on start and every given period
        this.actorSystem
                .scheduler()
                .schedule(
                        Duration.ofSeconds(SCHEDULED_DELAY), // delay
                        Duration.ofSeconds(SCHEDULED_REPETITION), // interval
                        () -> {
                            WSRequest request = ws.url(NATIONALITIES_API_URL);

                            request.get().thenComposeAsync((res) -> {
                                JsonNode node = res.getBody(json());
                                HashSet<Nationality> newNationalities = new HashSet<>();
                                for (JsonNode singleNode : node) {
                                    Nationality nationality = new Nationality(singleNode.get("name").asText());
                                    newNationalities.add(nationality);
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

