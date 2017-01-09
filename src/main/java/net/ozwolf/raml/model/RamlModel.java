package net.ozwolf.raml.model;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import net.ozwolf.raml.exception.RamlValidationException;
import net.ozwolf.raml.model.v08.V08_RamlModel;
import net.ozwolf.raml.model.v10.V10_RamlModel;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;

import java.net.URI;
import java.util.List;

@SuppressWarnings("Convert2MethodRef")
public interface RamlModel {
    String getTitle();

    String getVersion();

    List<String> getProtocols();

    boolean hasStylesheets();

    List<URI> getStylesheets();

    List<RamlDocumentationModel> getDocumentation();

    List<RamlSecurityModel> getSecurity();

    List<RamlResourceModel> getResources();

    static RamlModel create(String specificationFile, ApiSpecsConfiguration configuration) throws RamlValidationException {
        RamlModelResult result = new RamlModelBuilder().buildApi(specificationFile);

        if (result.hasErrors())
            throw new RamlValidationException("RAML specification is invalid.", result.getValidationResults());

        return result.isVersion08() ? new V08_RamlModel(result.getApiV08(), configuration) : new V10_RamlModel(result.getApiV10(), configuration);
    }
}
