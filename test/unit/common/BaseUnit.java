package unit.common;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.Helpers;
import play.test.WithApplication;


public class BaseUnit extends WithApplication {

    public static Application app;

    @BeforeClass
    public static void setup() {
        Module testModule = new AbstractModule() {
            @Override
            public void configure() {
            }
        };
        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()))
                .overrides(testModule);
        Guice.createInjector(builder.applicationModule());

        app = builder.build();

        Helpers.start(app);
    }


    /**
     * Tears down the fake app
     */
    @AfterClass
    public static void tearDown() {
        Helpers.stop(app);
    }

}
