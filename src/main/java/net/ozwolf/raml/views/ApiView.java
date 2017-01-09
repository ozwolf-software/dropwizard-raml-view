package net.ozwolf.raml.views;

import io.dropwizard.views.View;
import net.ozwolf.raml.model.RamlModel;

public class ApiView extends View {
    private final RamlModel model;

    public ApiView(RamlModel model) {
        super("../templates/index.ftl");
        this.model = model;
    }

    public RamlModel getModel() {
        return model;
    }
}
