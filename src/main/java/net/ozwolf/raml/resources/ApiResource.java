package net.ozwolf.raml.resources;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import net.ozwolf.raml.exception.RamlValidationException;
import net.ozwolf.raml.model.RamlModel;
import net.ozwolf.raml.views.ApiView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class ApiResource {
    private final String specificationFile;
    private final ApiSpecsConfiguration configuration;

    private final static Logger LOGGER = LoggerFactory.getLogger(ApiResource.class);

    public ApiResource(String specificationFile, ApiSpecsConfiguration configuration) {
        this.specificationFile = specificationFile;
        this.configuration = configuration;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public ApiView get() {
        try {
            return new ApiView(RamlModel.create(specificationFile, configuration));
        } catch (RamlValidationException e) {
            LOGGER.error("RAML specification is invalid.", e);
            throw new ServiceUnavailableException("RAML specification has errors.  See log for details.");
        }
    }

    @Path("/raw")
    @Produces("text/plain")
    public String getRaw(){
        throw new ServiceUnavailableException("This resource is currently not available due to pending work on the RAML Parser third party library.");
    }

    public static ApiResource resource(String specificationFile, ApiSpecsConfiguration configuration) {
        return new ApiResource(specificationFile, configuration);
    }
}
