package net.ozwolf.raml.model;

import java.util.List;

public interface RamlResponseModel {
    String getId();

    Integer getCode();

    String getDescription();

    List<RamlParameterModel> getHeaders();

    String getContentType();

    boolean isJson();

    String getExample();

    String getSchema();
}
