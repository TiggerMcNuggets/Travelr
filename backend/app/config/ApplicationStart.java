package config;

import javax.inject.*;

import models.User;
import play.inject.ApplicationLifecycle;
import play.Environment;
import repository.UserRepository;

import java.util.concurrent.CompletableFuture;

// This creates an `config.ApplicationStart` object once at start-up.
@Singleton
public class ApplicationStart {

    // Inject the application's Environment upon start-up and register hook(s) for shut-down.
    @Inject
    public ApplicationStart(ApplicationLifecycle lifecycle, Environment environment) {

        // Shut-down hook
        lifecycle.addStopHook( () -> {
            return CompletableFuture.completedFuture(null);
        } );

        // Insert admin user

        User user = User.find.findByEmail("admin@admin.com");

        if(user == null) {
            user = new User("Global", "Admin", "admin@admin.com", 1);
            user.setPassword("admin");
            user.setAccountType(2);
            user.insert();
        }
    }
}