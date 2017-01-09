package net.ozwolf.raml.model;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

public interface RamlMethodModel {
    String getType();
    String getDescription();
    List<RamlParameterModel> getParameters();
    List<RamlRequestModel> getRequests();
    List<RamlResponseModel> getResponses();
    List<RamlSecurityModel> getSecurity();
    boolean isDeprecated();
}
