package tasks;

import javax.inject.*;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.inject.ApplicationLifecycle;
import play.Environment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import play.libs.ws.*;
import play.mvc.Results;
import scala.concurrent.ExecutionContext;


// This creates an `tasks.ApplicationStart` object once at start-up.
@Singleton
public class NationalitiesTask implements WSBodyReadables, WSBodyWritables {

    private WSClient ws;
    private ActorSystem actorSystem;
    private ExecutionContext executionContext;

    // Inject the application's Environment upon start-up and register hook(s) for shut-down.
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

        this.actorSystem
                .scheduler()
                .schedule(
                        Duration.ofSeconds(60), // delay
                        Duration.ofSeconds(60), // interval
                        () -> {
                            WSRequest request = ws.url("https://restcountries.eu/rest/v2/all");

                            request.get().thenComposeAsync((res) -> {
                                JsonNode node = res.getBody(json());
                                System.out.println("---------------------------------------------------------");
                                System.out.println(res.getStatus());
                                System.out.println(node);
                                System.out.println("---------------------------------------------------------");
                                return CompletableFuture.completedFuture(Results.ok("ok"));
                            });
                        },
                        this.executionContext
                );




        // Shut-down hook
        lifecycle.addStopHook( () -> {
            return CompletableFuture.completedFuture(null);
        } );
    }

}

