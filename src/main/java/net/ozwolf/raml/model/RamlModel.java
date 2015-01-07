package net.ozwolf.raml.model;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import org.raml.model.Protocol;
import org.raml.model.Raml;
import org.raml.parser.visitor.RamlDocumentBuilder;

import java.net.URI;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public class RamlModel {
    private final Raml raml;
    private final ApiSpecsConfiguration configuration;

    public RamlModel(String specificationFile, ApiSpecsConfiguration configuration) {
        this.raml = new RamlDocumentBuilder().build(specificationFile);
        this.configuration = configuration;
    }

    public String getTitle() {
        return raml.getTitle();
    }

    public String getVersion() {
        return raml.getVersion();
    }

    public List<Protocol> getProtocols() {
        if (raml.getProtocols() == null || raml.getProtocols().isEmpty()) return newArrayList(Protocol.HTTP);
        return raml.getProtocols();
    }

    public boolean hasStylesheets() {
        return !configuration.getStylesheets().isEmpty();
    }

    public List<URI> getStylesheets() {
        return configuration.getStylesheets();
    }

    public List<RamlDocumentationModel> getDocumentation() {
        if (raml.getDocumentation() == null) return newArrayList();
        return raml.getDocumentation().stream()
                .map(RamlDocumentationModel::new)
                .collect(toList());
    }

    public List<RamlResourceModel> getResources() {
        if (raml.getResources() == null) return newArrayList();
        return raml.getResources().values().stream()
                .map(v -> new RamlResourceModel(v, getSecurity()))
                .collect(toList());
    }

    private List<RamlSecurityModel> getSecurity() {
        return raml.getSecuritySchemes().stream()
                .map(s -> new RamlSecurityModel(
                        s.entrySet().stream().findFirst().get().getKey(),
                        s.entrySet().stream().findFirst().get().getValue()
                ))
                .collect(toList());
    }

    @Override
    public String toString() {
        return String.format("RAML = [%s - v%s]", raml.getTitle(), raml.getVersion());
    }
}
