package net.ozwolf.raml.model;

import java.util.List;

public interface RamlRequestModel {
    String getId();

    String getContentType();

    List<RamlParameterModel> getHeaders();

    boolean isJson();

    String getExample();

    String getSchema();
}
