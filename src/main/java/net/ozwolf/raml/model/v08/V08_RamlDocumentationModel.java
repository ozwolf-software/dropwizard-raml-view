package net.ozwolf.raml.model.v08;

import net.ozwolf.raml.model.RamlDocumentationModel;
import org.apache.commons.codec.binary.Hex;
import org.raml.v2.api.model.v08.api.DocumentationItem;

import static net.ozwolf.raml.utils.MarkDownHelper.fromMarkDown;

public class V08_RamlDocumentationModel implements RamlDocumentationModel {
    private final DocumentationItem item;

    public V08_RamlDocumentationModel(DocumentationItem item) {
        this.item = item;
    }

    @Override
    public String getId() {
        return Hex.encodeHexString(item.title().getBytes());
    }

    @Override
    public String getTitle() {
        return item.title();
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
