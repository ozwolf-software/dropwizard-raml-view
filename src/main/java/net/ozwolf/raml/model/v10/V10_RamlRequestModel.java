package net.ozwolf.raml.model.v10;

import net.ozwolf.raml.model.RamlParameterModel;
import net.ozwolf.raml.model.RamlRequestModel;
import org.apache.commons.codec.binary.Hex;
import org.raml.v2.api.model.v10.datamodel.ExternalTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import javax.ws.rs.core.MediaType;
import java.util.List;

import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class V10_RamlRequestModel implements RamlRequestModel {
    private final TypeDeclaration body;
    private final List<RamlParameterModel> headers;

    public V10_RamlRequestModel(TypeDeclaration body, List<RamlParameterModel> headers) {
        this.body = body;
        this.headers = headers;
    }

    @Override
    public String getId() {
        return Hex.encodeHexString(body.name().getBytes());
    }

    @Override
    public String getContentType() {
        return body.name();
    }

    @Override
    public List<RamlParameterModel> getHeaders() {
        return headers;
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
        return (body instanceof ExternalTypeDeclaration) ? ((ExternalTypeDeclaration) body).schemaContent() : null;
    }

    @Override
    public String toString(){
        return String.format("Request = [%s]", getContentType());
    }
}
