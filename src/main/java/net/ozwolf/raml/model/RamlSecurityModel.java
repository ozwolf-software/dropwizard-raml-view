package net.ozwolf.raml.model;

import org.apache.commons.codec.binary.Hex;
import org.raml.model.SecurityScheme;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

public class RamlSecurityModel {
    private final String name;
    private final SecurityScheme security;

    public RamlSecurityModel(String name, SecurityScheme security) {
        this.name = name;
        this.security = security;
    }

    public String getId() {
        return Hex.encodeHexString(security.getType().getBytes());
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return security.getType();
    }

    public List<RamlHeaderModel> getHeaders() {
        if (security.getDescribedBy() == null || security.getDescribedBy().getHeaders() == null) return newArrayList();
        return security.getDescribedBy().getHeaders().entrySet().stream()
                .map(e -> new RamlHeaderModel(e.getKey(), e.getValue()))
                .collect(toList());
    }

    public List<RamlParameterModel> getQueryParameters() {
        if (security.getDescribedBy() == null || security.getDescribedBy().getQueryParameters() == null)
            return newArrayList();
        return security.getDescribedBy().getQueryParameters().entrySet().stream()
                .map(e -> new RamlParameterModel(e.getKey(), e.getValue()))
                .collect(toList());
    }

    public Map<Integer, String> getResponses() {
        Map<Integer, String> responses = newHashMap();
        if (security.getDescribedBy() == null || security.getDescribedBy().getResponses() == null) return responses;

        security.getDescribedBy().getResponses().entrySet().stream()
                .forEach(e -> {
                    Integer code = Integer.valueOf(e.getKey());
                    String description = e.getValue().getDescription() == null ? "" : e.getValue().getDescription();
                    responses.put(code, description);
                });
        return responses;
    }

    public static RamlSecurityModel model(String name, SecurityScheme security) {
        return new RamlSecurityModel(name, security);
    }

    @Override
    public String toString() {
        return String.format("Security = [%s]", getType());
    }

}
