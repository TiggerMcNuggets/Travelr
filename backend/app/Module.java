import com.google.inject.AbstractModule;
import config.ApplicationStart;

public class Module extends AbstractModule {
    protected void configure() {
        bind(ApplicationStart.class).asEagerSingleton();
    }
}