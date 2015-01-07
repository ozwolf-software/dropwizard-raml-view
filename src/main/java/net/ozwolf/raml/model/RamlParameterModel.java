package net.ozwolf.raml.model;

import org.apache.commons.lang.StringUtils;
import org.raml.model.parameter.AbstractParam;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;
import org.raml.model.parameter.UriParameter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class RamlParameterModel {
    private final String name;
    private final AbstractParam parameter;

    public RamlParameterModel(String name, AbstractParam parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public String getParameterType() {
        if (parameter instanceof UriParameter) return "path";
        if (parameter instanceof QueryParameter) return "query";
        if (parameter instanceof Header) return "header";
        return "unknown";
    }

    public String getDataType() {
        return parameter.getType().name().toLowerCase();
    }

    public String getFlags() {
        List<String> flags = newArrayList(parameter.isRequired() ? "required" : "optional");
        if (parameter.isRepeat()) flags.add("repeatable");
        return StringUtils.join(flags, ", ");
    }

    public String getDescription() {
        return fromMarkDown(parameter.getDescription());
    }

    public String getPattern() {
        return parameter.getPattern();
    }

    public String getExample() {
        return parameter.getExample();
    }

    public String getDefault() {
        return parameter.getDefaultValue();
    }

    public List<String> getAllowedValues() {
        return parameter.getEnumeration();
    }

    public String getDisplay() {
        if (parameter.getPattern() != null) return parameter.getPattern();
        if (parameter.getExample() != null) return parameter.getExample();
        return "value";
    }

    @Override
    public String toString() {
        return String.format("Parameter = [%s - %s]", getName(), getParameterType());
    }
}
