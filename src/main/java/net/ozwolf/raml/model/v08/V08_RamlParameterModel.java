package net.ozwolf.raml.model.v08;

import net.ozwolf.raml.model.RamlParameterModel;
import org.apache.commons.lang3.StringUtils;
import org.raml.v2.api.model.v08.parameters.Parameter;
import org.raml.v2.api.model.v08.parameters.StringTypeDeclaration;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public abstract class V08_RamlParameterModel implements RamlParameterModel {
    private final Parameter parameter;

    public V08_RamlParameterModel(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getName() {
        return parameter.name();
    }

    @Override
    public String getDataType() {
        return parameter.type();
    }

    @Override
    public String getFlags() {
        List<String> flags = newArrayList(parameter.required() != null && parameter.required() ? "required" : "optional");
        if (parameter.repeat() != null && parameter.repeat()) flags.add("repeatable");
        return StringUtils.join(flags, ", ");
    }

    @Override
    public String getDescription() {
        return fromMarkDown(parameter.description().value());
    }

    @Override
    public String getDisplay() {
        if (getPattern() != null) return getPattern();
        if (getExample() != null) return getExample();
        return parameter.name();
    }

    @Override
    public String getPattern() {
        return (parameter instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) parameter).pattern() : null;
    }

    @Override
    public String getExample() {
        return parameter.example();
    }

    @Override
    public String getDefault() {
        return parameter.defaultValue();
    }

    @Override
    public List<String> getAllowedValues() {
        return (parameter instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) parameter).enumValues() : newArrayList();
    }

    @Override
    public String toString() {
        return String.format("Parameter = [%s - %s]", getName(), getParameterType());
    }

    public static class Path extends V08_RamlParameterModel implements RamlParameterModel {
        public Path(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String getParameterType() {
            return "path";
        }
    }

    public static class Query extends V08_RamlParameterModel implements RamlParameterModel {
        public Query(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String getParameterType() {
            return "query";
        }
    }

    public static class Header extends V08_RamlParameterModel implements RamlParameterModel {
        public Header(Parameter parameter) {
            super(parameter);
        }

        @Override
        public String getParameterType() {
            return "header";
        }
    }
}
