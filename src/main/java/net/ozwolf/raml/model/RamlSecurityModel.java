package net.ozwolf.raml.model;

import java.util.List;
import java.util.Map;

public interface RamlSecurityModel {
    String getId();

    String getName();

    String getType();

    List<RamlParameterModel> getHeaders();

    List<RamlParameterModel> getQueryParameters();

    Map<String, String> getResponses();
}
