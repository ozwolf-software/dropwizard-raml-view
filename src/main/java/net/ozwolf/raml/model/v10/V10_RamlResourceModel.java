package net.ozwolf.raml.model.v10;

import net.ozwolf.raml.model.RamlMethodModel;
import net.ozwolf.raml.model.RamlResourceModel;
import net.ozwolf.raml.model.RamlSecurityModel;
import org.apache.commons.codec.binary.Hex;
import org.raml.v2.api.model.v10.resources.Resource;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class V10_RamlResourceModel implements RamlResourceModel {
    private final Resource resource;
    private final List<RamlSecurityModel> securitySchemes;

    public V10_RamlResourceModel(Resource resource, List<RamlSecurityModel> securitySchemes) {
        this.resource = resource;
        this.securitySchemes = securitySchemes;
    }

    @Override
    public String getId() {
        return Hex.encodeHexString(resource.relativeUri().value().getBytes());
    }

    @Override
    public String getUri() {
        return resource.resourcePath();
    }

    @Override
    public String getDisplayName() {
        return resource.displayName().value();
    }

    @Override
    public List<RamlMethodModel> getActions() {
        return resource.methods().stream().map(m -> new V10_RamlMethodModel(m, securitySchemes)).collect(toList());
    }

    @Override
    public List<RamlResourceModel> getResources() {
        return resource.resources().stream().map(r -> new V10_RamlResourceModel(r, securitySchemes)).collect(toList());
    }

    @Override
    public String getDescription() {
        return fromMarkDown(resource.description().value());
    }
}
