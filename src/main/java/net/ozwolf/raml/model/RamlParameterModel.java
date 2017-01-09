package net.ozwolf.raml.model;

import java.util.List;

public interface RamlParameterModel {
    String getName();

    String getParameterType();

    String getDataType();

    String getFlags();

    String getDescription();

    String getDisplay();

    String getPattern();

    String getExample();

    String getDefault();

    List<String> getAllowedValues();
}
