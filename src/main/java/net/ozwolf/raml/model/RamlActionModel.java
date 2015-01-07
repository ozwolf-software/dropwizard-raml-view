package net.ozwolf.raml.model;

import org.raml.model.Action;
import org.raml.model.MimeType;
import org.raml.model.SecurityReference;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class RamlActionModel {
    private final Action action;
    private final List<RamlSecurityModel> securityModels;

    public RamlActionModel(Action action, List<RamlSecurityModel> securityModels) {
        this.action = action;
        this.securityModels = securityModels;
    }

    public String getType() {
        return action.getType().name();
    }

    public String getDescription() {
        if (action.getDescription() == null) return null;
        return fromMarkDown(action.getDescription());
    }

    public List<RamlParameterModel> getParameters() {
        List<RamlParameterModel> parameters = newArrayList();

        if (action.getResource().getResolvedUriParameters() != null)
            parameters.addAll(
                    action.getResource()
                            .getResolvedUriParameters().entrySet().stream()
                            .map(e -> new RamlParameterModel(e.getKey(), e.getValue()))
                            .collect(toList())
            );

        if (action.getQueryParameters() != null)
            parameters.addAll(
                    action.getQueryParameters().entrySet().stream()
                            .map(e -> new RamlParameterModel(e.getKey(), e.getValue()))
                            .collect(toList())
            );

        if (action.getHeaders() != null)
            parameters.addAll(
                    action.getHeaders().entrySet().stream()
                            .map(e -> new RamlParameterModel(e.getKey(), e.getValue()))
                            .collect(toList())
            );

        return parameters;
    }

    public List<RamlRequestModel> getRequests() {
        if (action.getBody() == null) return newArrayList();
        final List<RamlHeaderModel> headers = newArrayList();
        if (action.getHeaders() != null)
            headers.addAll(action.getHeaders().entrySet()
                    .stream()
                    .map(e -> new RamlHeaderModel(e.getKey(), e.getValue()))
                    .collect(toList()));
        return action.getBody().values().stream().map(v -> new RamlRequestModel(v, headers)).collect(toList());
    }

    public List<RamlResponseModel> getResponses() {
        if (action.getResponses() == null) return newArrayList();
        return newArrayList(action.getResponses().entrySet())
                .stream()
                .map(
                        e -> {
                            List<RamlResponseModel> models = newArrayList();

                            Integer code = Integer.valueOf(e.getKey());
                            String description = e.getValue().getDescription();
                            Map<String, MimeType> body = e.getValue().getBody();
                            List<RamlHeaderModel> headers = e.getValue().getHeaders().entrySet().stream()
                                    .map(h -> new RamlHeaderModel(h.getKey(), h.getValue()))
                                    .collect(toList());

                            body.entrySet().stream()
                                    .forEach(m -> models.add(new RamlResponseModel(code, description, headers, m.getValue())));

                            return models;
                        }
                )
                .reduce(newArrayList(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });
    }

    public List<RamlSecurityModel> getSecurity() {
        return securityModels.stream()
                .filter(m -> action.getSecuredBy().stream().map(SecurityReference::getName).anyMatch(s -> s.equals(m.getName())))
                .collect(toList());
    }

    public boolean isDeprecated() {
        return action.getIs().contains("deprecated");
    }
}
