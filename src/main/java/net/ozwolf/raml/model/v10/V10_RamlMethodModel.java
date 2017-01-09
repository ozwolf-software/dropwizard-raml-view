package net.ozwolf.raml.model.v10;

import net.ozwolf.raml.model.*;
import org.raml.v2.api.model.v10.methods.Method;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class V10_RamlMethodModel implements RamlMethodModel {
    private final Method method;
    private final List<RamlSecurityModel> securitySchemes;

    @SuppressWarnings("ConstantConditions")
    public V10_RamlMethodModel(Method method, List<RamlSecurityModel> securitySchemes) {
        this.method = method;
        this.securitySchemes = securitySchemes;
    }

    @Override
    public String getType() {
        return method.method().toUpperCase();
    }

    @Override
    public String getDescription() {
        return fromMarkDown(method.description().value());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public List<RamlParameterModel> getParameters() {
        List<RamlParameterModel> parameters = newArrayList();

        if (method.resource().uriParameters() != null)
            method.resource().uriParameters().stream().forEach(p -> parameters.add(new V10_RamlParameterModel.Path(p)));

        if (method.queryParameters() != null)
            method.queryParameters().stream().forEach(p -> parameters.add(new V10_RamlParameterModel.Query(p)));

        if (method.headers() != null)
            method.headers().stream().forEach(p -> parameters.add(new V10_RamlParameterModel.Header(p)));

        return parameters;
    }

    @Override
    public List<RamlRequestModel> getRequests() {
        List<RamlParameterModel> headers = method.headers() != null ? method.headers().stream().map(V10_RamlParameterModel.Header::new).collect(toList()) : newArrayList();

        return method.body() != null ?
                method.body().stream().map(b -> new V10_RamlRequestModel(b, headers)).collect(toList()) :
                newArrayList();
    }

    @Override
    public List<RamlResponseModel> getResponses() {
        return method.responses() != null ?
                method.responses()
                        .stream()
                        .flatMap(r -> {
                            Integer code = Integer.valueOf(r.code().value());
                            String description = r.description().value();
                            List<RamlParameterModel> responseHeaders = r.headers() != null ? r.headers().stream().map(V10_RamlParameterModel.Header::new).collect(toList()) : newArrayList();
                            return r.body().stream().map(b -> new V10_RamlResponseModel(b, code, description, responseHeaders));
                        })
                        .collect(toList()) :
                newArrayList();
    }

    @Override
    public List<RamlSecurityModel> getSecurity() {
        return securitySchemes.stream()
                .filter(s -> method.securedBy() != null && method.securedBy().stream().anyMatch(m -> m.name().equalsIgnoreCase(s.getName())))
                .collect(toList());
    }

    @Override
    public boolean isDeprecated() {
        return method.is().stream().anyMatch(t -> t.name().equalsIgnoreCase("deprecated"));
    }
}
