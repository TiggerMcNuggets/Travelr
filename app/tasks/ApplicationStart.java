package tasks;

import javax.inject.*;

import com.typesafe.config.Config;
import models.User;
import play.inject.ApplicationLifecycle;
import play.Environment;

import java.util.concurrent.CompletableFuture;

// This creates an `tasks.ApplicationStart` object once at start-up.
@Singleton
public class ApplicationStart {

    // Inject the application's Environment upon start-up and register hook(s) for shut-down.
    @Inject
    public ApplicationStart(ApplicationLifecycle lifecycle, Environment environment, Config config) {

            User user = User.find.findByEmail("admin@admin.com");

            if(user == null) {
                user = new User("Global", "Admin", "admin@admin.com", 1);
                user.setPassword("admin");
                user.setAccountType(2);
                user.insert();
            }


        // Shut-down hook
        lifecycle.addStopHook( () -> CompletableFuture.completedFuture(null));


    }
}