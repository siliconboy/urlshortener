package com.codepath.urlshortener;

import com.codepath.urlshortener.resources.URLShortenerResources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class URLShortenerApplication extends Application<URLShortenerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new URLShortenerApplication().run(args);
    }

    @Override
    public String getName() {
        return "urlshortener";
    }

    @Override
    public void initialize(final Bootstrap<URLShortenerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final URLShortenerConfiguration configuration,
                    final Environment environment) {
        
        final URLShortenerResources resource = new URLShortenerResources(
        configuration.getTemplate(),
        configuration.getDefaultURL()
    );

    environment.jersey().register(resource);
    }

}
