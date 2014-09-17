package net.ozwolf.raml.model;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import com.googlecode.totallylazy.Sequence;
import org.raml.model.Protocol;
import org.raml.model.Raml;

import java.net.URI;
import java.util.List;

import static net.ozwolf.raml.utils.TotallyLazyHelper.*;
import static com.googlecode.totallylazy.Sequences.sequence;

public class RamlModel {
    private final Raml raml;
    private final ApiSpecsConfiguration configuration;

    public RamlModel(Raml raml, ApiSpecsConfiguration configuration) {
        this.raml = raml;
        this.configuration = configuration;
    }

    public String getTitle() {
        return raml.getTitle();
    }

    public String getVersion() {
        return raml.getVersion();
    }

    public Sequence<Protocol> getProtocols() {
        if (raml.getProtocols() == null || raml.getProtocols().isEmpty()) return sequence(Protocol.HTTP);
        return sequence(raml.getProtocols());
    }

    public boolean hasStylesheets() {
        return !configuration.getStylesheets().isEmpty();
    }

    public List<URI> getStylesheets() {
        return configuration.getStylesheets();
    }

    public Sequence<RamlDocumentationModel> getDocumentation() {
        if (raml.getDocumentation() == null) return sequence();
        return sequence(raml.getDocumentation()).map(asRamlDocumentationModel());
    }

    public Sequence<RamlResourceModel> getResources() {
        if (raml.getResources() == null) return sequence();
        return sequence(raml.getResources().values()).map(asRamlResourceModel(getSecurity()));
    }

    private Sequence<RamlSecurityModel> getSecurity() {
        return sequence(raml.getSecuritySchemes()).map(asRamlSecurityModel());
    }

    @Override
    public String toString() {
        return String.format("RAML = [%s - v%s]", raml.getTitle(), raml.getVersion());
    }

    public static RamlModel model(Raml raml, ApiSpecsConfiguration configuration) {
        return new RamlModel(raml, configuration);
    }
}
