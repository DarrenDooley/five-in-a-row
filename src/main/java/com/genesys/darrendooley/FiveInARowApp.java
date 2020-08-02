package com.genesys.darrendooley;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FiveInARowApp extends Application<AppConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Override
    public void run(AppConfiguration c, Environment e) {
        LOGGER.info("Registering REST resources");
        e.jersey().register(new RequestHandler());
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<AppConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new FiveInARowApp().run(args);
    }
}