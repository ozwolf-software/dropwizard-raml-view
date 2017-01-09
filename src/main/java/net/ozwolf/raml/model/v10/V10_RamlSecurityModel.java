package net.ozwolf.raml.model.v10;

import net.ozwolf.raml.model.RamlParameterModel;
import net.ozwolf.raml.model.RamlResponseModel;
import net.ozwolf.raml.model.RamlSecurityModel;
import org.apache.commons.codec.binary.Hex;
import org.raml.v2.api.model.v10.security.SecurityScheme;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.stream.Collectors.toList;

public class V10_RamlSecurityModel implements RamlSecurityModel {
    private final SecurityScheme scheme;

    public V10_RamlSecurityModel(SecurityScheme scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getId() {
        return Hex.encodeHexString(scheme.name().getBytes());
    }

    @Override
    public String getName() {
        return scheme.name();
    }

    @Override
    public String getType() {
        return scheme.type();
    }

    @Override
    public List<RamlParameterModel> getHeaders() {
        if (scheme.describedBy() != null && scheme.describedBy().headers() != null)
            return scheme.describedBy().headers().stream().map(V10_RamlParameterModel.Header::new).collect(toList());

        return newArrayList();
    }

    @Override
    public List<RamlParameterModel> getQueryParameters() {
        if (scheme.describedBy() != null && scheme.describedBy().queryParameters() != null)
            return scheme.describedBy().queryParameters().stream().map(V10_RamlParameterModel.Query::new).collect(toList());

        return newArrayList();
    }



    @Override
    public Map<String, String> getResponses() {
        Map<String, String> responses = newHashMap();
        if (scheme.describedBy() != null && scheme.describedBy().responses() != null)
            scheme.describedBy().responses()
                    .stream()
                    .forEach(r -> responses.put(r.code().value(), r.description().value()));

        return responses;
    }
}
