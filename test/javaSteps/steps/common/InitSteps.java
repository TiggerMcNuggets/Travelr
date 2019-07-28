package javaSteps.steps.common;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import javaSteps.models.StateSingleton;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.Helpers;
import play.test.WithApplication;

/**
 * Contains set up and tear down methods that are called every scenario
 */
public class InitSteps extends WithApplication {
    // Singleton object that holds shared values across features
    @Inject
    private StateSingleton state = StateSingleton.getInstance();

    /**
     * Creates an application builder and starts the fake app
     */
    @Before
    public void setUp() {
        Module testModule = new AbstractModule() {
            @Override
            public void configure() {
            }
        };
        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()))
                .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);

        Helpers.start(state.getApplication());
        state.setRequest(Helpers.fakeRequest());
    }


    /**
     * Tears down the fake app
     */
    @After
    public void tearDown() {
        Helpers.stop(state.getApplication());
    }
}
