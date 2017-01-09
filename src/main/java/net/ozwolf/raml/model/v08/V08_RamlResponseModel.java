package net.ozwolf.raml.model.v08;

import net.ozwolf.raml.model.RamlParameterModel;
import net.ozwolf.raml.model.RamlResponseModel;
import org.apache.commons.codec.binary.Hex;
import org.raml.v2.api.model.v08.bodies.BodyLike;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class V08_RamlResponseModel implements RamlResponseModel {
    private final BodyLike body;
    private final Integer code;
    private final String description;
    private final List<RamlParameterModel> headers;

    public V08_RamlResponseModel(BodyLike body, Integer code, String description, List<RamlParameterModel> headers) {
        this.body = body;
        this.code = code;
        this.description = description;
        this.headers = headers;
    }

    @Override
    public String getId() {
        return Hex.encodeHexString((code + body.name()).getBytes());
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return fromMarkDown(description);
    }

    @Override
    public List<RamlParameterModel> getHeaders() {
        return headers;
    }

    @Override
    public String getContentType() {
        return body.name();
    }

    @Override
    public boolean isJson() {
        return MediaType.APPLICATION_JSON_TYPE.isCompatible(MediaType.valueOf(body.name()));
    }

    @Override
    public String getExample() {
        return body.example().value();
    }

    @Override
    public String getSchema() {
        return body.schemaContent();
    }
}
