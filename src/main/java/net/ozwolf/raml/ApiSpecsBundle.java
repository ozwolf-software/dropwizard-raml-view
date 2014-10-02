package net.ozwolf.raml;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import net.ozwolf.raml.resources.ApiResource;
import io.dropwizard.Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;

import static net.ozwolf.raml.configuration.ApiSpecsConfiguration.configuration;

/**
 * # RAML Docs Bundle
 *
 * This bundle is a DropWizard bundle that displays a RAML API specification
 *
 * ## Example Usage
 *
 * ```java
 * bootstrap.addBundle(ApiSpecificationBundle.bundle("apispecs/apispecs.raml"));
 * ```
 */
public class ApiSpecsBundle implements Bundle {
    private final Raml raml;
    private final ApiSpecsConfiguration configuration;

    private ApiSpecsBundle(String specificationFile, ApiSpecsConfiguration configuration) {
        this.raml = new RamlDocumentBuilder().build(specificationFile);
        this.configuration = configuration;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new AssetsBundle("/raml-docs-assets", "/api/assets", null, "raml-view"));
    }

    @Override
    public void run(Environment environment) {
        environment.jersey().register(ApiResource.resource(raml, configuration));
    }

    /**
     * Instantiate the RAML API documentation bundle.
     *
     * @param specificationFile The path to the specification resource
     * @return The DropWizard bundle
     */
    public static ApiSpecsBundle bundle(String specificationFile) {
        return new ApiSpecsBundle(specificationFile, configuration());
    }

    /**
     * Instantiate the RAML API documentation bundle with a custom configuration.
     *
     * @param specificationFile The path to the specification resource
     * @param configuration     The API specs configuration
     * @return The DropWizard bundle.
     */
    public static ApiSpecsBundle bundle(String specificationFile, ApiSpecsConfiguration configuration) {
        return new ApiSpecsBundle(specificationFile, configuration);
    }
}
