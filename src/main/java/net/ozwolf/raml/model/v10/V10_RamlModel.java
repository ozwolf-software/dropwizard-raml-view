package net.ozwolf.raml.model.v10;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import net.ozwolf.raml.model.RamlDocumentationModel;
import net.ozwolf.raml.model.RamlModel;
import net.ozwolf.raml.model.RamlResourceModel;
import net.ozwolf.raml.model.RamlSecurityModel;
import org.raml.v2.api.model.v10.api.Api;

import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class V10_RamlModel implements RamlModel {
    private final Api api;
    private final ApiSpecsConfiguration configuration;

    public V10_RamlModel(Api api, ApiSpecsConfiguration configuration) {
        this.api = api;
        this.configuration = configuration;
    }

    @Override
    public String getTitle() {
        return api.title().value();
    }

    @Override
    public String getVersion() {
        return api.version().value();
    }

    @Override
    public List<String> getProtocols() {
        return api.protocols();
    }

    @Override
    public boolean hasStylesheets() {
        return !configuration.getStylesheets().isEmpty();
    }

    @Override
    public List<URI> getStylesheets() {
        return configuration.getStylesheets();
    }

    @Override
    public List<RamlDocumentationModel> getDocumentation() {
        if (api.documentation() != null)
            return api.documentation().stream().map(V10_RamlDocumentationModel::new).collect(toList());
        return newArrayList();
    }

    @Override
    public List<RamlSecurityModel> getSecurity() {
        if (api.securitySchemes() != null)
            return api.securitySchemes().stream().map(V10_RamlSecurityModel::new).collect(toList());

        return newArrayList();
    }

    @Override
    public List<RamlResourceModel> getResources() {
        if (api.resources() != null)
            return api.resources().stream().map(r -> new V10_RamlResourceModel(r, getSecurity())).collect(toList());

        return newArrayList();
    }

    @Override
    public String toString() {
        return String.format("RAML = [%s - v%s]", getTitle(), getVersion());
    }
}
