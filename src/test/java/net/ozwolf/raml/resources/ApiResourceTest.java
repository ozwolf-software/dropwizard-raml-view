package net.ozwolf.raml.resources;

import net.ozwolf.raml.model.RamlModel;
import net.ozwolf.raml.views.ApiView;
import junitx.util.PrivateAccessor;
import org.junit.Test;
import org.raml.model.Raml;

import static net.ozwolf.raml.configuration.ApiSpecsConfiguration.configuration;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class ApiResourceTest {
    private final Raml raml = mock(Raml.class);

    @Test
    public void shouldReturnViewWithRamlModel() throws NoSuchFieldException {
        ApiView view = new ApiResource(raml, configuration()).get();

        RamlModel model = (RamlModel) PrivateAccessor.getField(view, "model");
        Raml result = (Raml) PrivateAccessor.getField(model, "raml");

        assertThat(result, is(raml));
    }
}
