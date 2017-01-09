package net.ozwolf.raml.model.sanity;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.RamlView;

public class SanityTestApp extends Application<SanityTestConfiguration> {
    private final String resource;

    public SanityTestApp(String resource) {
        this.resource = resource;
    }

    @Override
    public String getName() {
        return "sanity-test-app";
    }

    @Override
    public void initialize(Bootstrap<SanityTestConfiguration> bootstrap) {
        bootstrap.addBundle(RamlView.bundle(resource));
    }

    @Override
    public void run(SanityTestConfiguration configuration,
                    Environment environment) throws Exception {

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1)
            throw new IllegalArgumentException("usage: SanityTestApp <ramlVersion>");

        String specificationFile = String.format("apispecs-test/%s/apispecs.raml", args[0]);

        if (SanityTestApp.class.getClassLoader().getResource(specificationFile) == null)
            throw new IllegalArgumentException(String.format("No specification file found at [ %s ].", specificationFile));

        System.out.println("***************************************************");
        System.out.println("*                                                 *");
        System.out.println("*            Running Sanity Test App              *");
        System.out.println("*                                                 *");
        System.out.println("***************************************************");
        System.out.println("");
        System.out.println("Specification File: " + specificationFile);
        System.out.println("              Port: 3500");
        System.out.println("        Admin Port: 3501");
        System.out.println("");

        new SanityTestApp(specificationFile).run("server", "conf/sanity-test.yml");
    }
}
