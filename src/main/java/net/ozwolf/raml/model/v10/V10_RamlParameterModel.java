package net.ozwolf.raml.model.v10;

import net.ozwolf.raml.model.RamlParameterModel;
import org.raml.v2.api.model.v10.datamodel.StringTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public abstract class V10_RamlParameterModel implements RamlParameterModel {
    private final TypeDeclaration parameter;

    public V10_RamlParameterModel(TypeDeclaration parameter) {
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
        return parameter.required() != null && parameter.required() ? "required" : "optional";
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
        return parameter.example().value();
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

    public static class Path extends V10_RamlParameterModel implements RamlParameterModel {
        public Path(TypeDeclaration parameter) {
            super(parameter);
        }

        @Override
        public String getParameterType() {
            return "path";
        }
    }

    public static class Query extends V10_RamlParameterModel implements RamlParameterModel {
        public Query(TypeDeclaration parameter) {
            super(parameter);
        }

        @Override
        public String getParameterType() {
            return "query";
        }
    }

    public static class Header extends V10_RamlParameterModel implements RamlParameterModel {
        public Header(TypeDeclaration parameter) {
            super(parameter);
        }

        @Override
        public String getParameterType() {
            return "header";
        }
    }
}
