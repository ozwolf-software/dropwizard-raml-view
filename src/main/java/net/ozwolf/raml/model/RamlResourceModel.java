package net.ozwolf.raml.model;

import java.util.List;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public interface RamlResourceModel {
    String getId();

    String getUri();

    String getDisplayName();

    List<RamlMethodModel> getActions();

    List<RamlResourceModel> getResources();

    String getDescription();

}
