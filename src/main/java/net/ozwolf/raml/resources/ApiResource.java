package net.ozwolf.raml.resources;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import net.ozwolf.raml.model.RamlModel;
import net.ozwolf.raml.views.ApiView;
import org.raml.emitter.RamlEmitter;
import org.raml.model.Raml;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class ApiResource {
    private final Raml raml;
    private final ApiSpecsConfiguration configuration;

    public ApiResource(Raml raml, ApiSpecsConfiguration configuration) {
        this.raml = raml;
        this.configuration = configuration;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public ApiView get() {
        return ApiView.view(RamlModel.model(raml, configuration));
    }

    @Path("/raw")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String raw() {
        return new RamlEmitter().dump(raml);
    }

    public static ApiResource resource(Raml raml, ApiSpecsConfiguration configuration) {
        return new ApiResource(raml, configuration);
    }
}
