package net.ozwolf.raml.model.v10;

import net.ozwolf.raml.model.RamlDocumentationModel;
import org.apache.commons.codec.binary.Hex;
import org.raml.v2.api.model.v10.api.DocumentationItem;

import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class V10_RamlDocumentationModel implements RamlDocumentationModel {
    private final DocumentationItem item;

    public V10_RamlDocumentationModel(DocumentationItem item) {
        this.item = item;
    }

    @Override
    public String getId() {
        return Hex.encodeHexString(item.title().value().getBytes());
    }

    @Override
    public String getTitle() {
        return item.title().value();
    }

    @Override
    public String getContent() {
        return fromMarkDown(item.content().value());
    }

    @Override
    public String toString() {
        return String.format("Documentation = [%s]", getTitle());
    }
}
