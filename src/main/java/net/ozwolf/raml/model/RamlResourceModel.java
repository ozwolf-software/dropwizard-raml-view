package net.ozwolf.raml.model;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.raml.model.Resource;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class RamlResourceModel {
    private final Resource resource;
    private final List<RamlSecurityModel> security;

    public RamlResourceModel(Resource resource, List<RamlSecurityModel> security) {
        this.resource = resource;
        this.security = security;
    }

    public String getId() {
        return Hex.encodeHexString(getUri().getBytes());
    }

    public String getUri() {
        return resource.getParentUri() + resource.getRelativeUri();
    }

    public String getDisplayName() {
        String defaultDisplayName = getUri().equals("/") ? "Root" : newArrayList(getUri().split("/")).stream().reduce((a, b) -> b).get();
        return StringUtils.defaultString(resource.getDisplayName(), defaultDisplayName);
    }

    public List<RamlActionModel> getActions() {
        if (resource.getActions() == null) return newArrayList();
        return resource.getActions().values().stream()
                .map(a -> new RamlActionModel(a, security))
                .collect(toList());
    }

    public List<RamlResourceModel> getResources() {
        if (resource.getResources() == null) return newArrayList();
        return resource.getResources().values().stream()
                .map(r -> new RamlResourceModel(r, security))
                .collect(toList());
    }

    public String getDescription() {
        if (resource.getDescription() == null) return null;
        return fromMarkDown(resource.getDescription());
    }

    @Override
    public String toString() {
        return String.format("Resource = [%s]", getUri());
    }
}
