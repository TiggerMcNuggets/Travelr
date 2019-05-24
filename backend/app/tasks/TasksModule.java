package tasks;

import com.google.inject.AbstractModule;

public class TasksModule extends AbstractModule {
    protected void configure() {
        bind(NationalitiesTask.class).asEagerSingleton();
    }
}