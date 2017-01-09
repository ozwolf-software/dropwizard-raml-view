package net.ozwolf.raml.model;

import net.ozwolf.raml.configuration.ApiSpecsConfiguration;
import net.ozwolf.raml.exception.RamlValidationException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ModelTest {
    private final RamlModel model;

    protected ModelTest(String resource) {
        try {
            this.model = RamlModel.create(resource, ApiSpecsConfiguration.configuration());
        } catch (RamlValidationException e) {
            throw new RuntimeException(StringUtils.join(e.getErrors(), ", "));
        }
    }

    @Test
    public void shouldLoadRamlAndExposeModelInformationForView() {
        assertThat(model.getTitle()).isEqualTo("Test API");
        assertThat(model.getVersion()).isEqualTo("1.0-SNAPSHOT");
        assertThat(model.getProtocols()).containsOnlyOnce("HTTP");
    }

    @Test
    public void shouldLoadResourcesAsModels() {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources).hasSize(1);

        assertThat(resources.get(0).getId()).isEqualTo("2f626f6f6b73");
        assertThat(resources.get(0).getDisplayName()).isEqualTo("Books");
        assertThat(resources.get(0).getDescription()).isEqualTo("<p>resource for getting and managing <code>books</code></p>\n");
        assertThat(resources.get(0).getUri()).isEqualTo("/books");

        assertThat(resources.get(0).getActions()).hasSize(2);

        assertThat(resources.get(0).getResources()).hasSize(1);

        assertThat(resources.get(0).getResources().get(0).getId()).isEqualTo("2f7b69647d");
        assertThat(resources.get(0).getResources().get(0).getActions()).hasSize(1);
    }

    @Test
    public void shouldLoadDocumentation() {
        List<RamlDocumentationModel> documentation = model.getDocumentation();

        assertThat(documentation).hasSize(1);

        assertThat(documentation.get(0).getId()).isEqualTo("53756d6d617279");
        assertThat(documentation.get(0).getTitle()).isEqualTo("Summary");
        assertThat(documentation.get(0).getContent()).isEqualTo("<h2>Summary</h2>\n<p>This API specification is for a test purposes only.  This should use <code>markdown4j</code> to parse the Markdown.</p>\n");
    }

    @Test
    public void shouldLoadPostActionInformation() throws IOException, JSONException {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources).hasSize(1);

        assertThat(resources.get(0).getUri()).isEqualTo("/books");

        List<RamlMethodModel> actions = resources.get(0).getActions();

        assertThat(actions).hasSize(2);

        RamlMethodModel post = actions.stream().filter(forType("POST")).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No [POST] action found on resource"));

        // Test basic action information
        assertThat(post.getType()).isEqualToIgnoringCase("POST");
        assertThat(post.getDescription()).isEqualTo("<p>add a book to the collection</p>\n");
        assertThat(post.isDeprecated()).isEqualTo(true);

        // Test security information
        assertThat(post.getSecurity()).hasSize(1);
        verifyBasicSecurity(post.getSecurity().get(0));

        // Test request information
        assertThat(post.getRequests()).hasSize(1);
        assertThat(post.getRequests().get(0).getId()).isEqualTo("6170706c69636174696f6e2f6a736f6e");
        assertThat(post.getRequests().get(0).getContentType()).isEqualTo("application/json");
        assertThat(post.getRequests().get(0).isJson()).isTrue();

        assertThat(post.getRequests().get(0).getHeaders()).hasSize(1);
        assertThat(post.getRequests().get(0).getHeaders().get(0).getName()).isEqualTo("x-call-id");
        assertThat(post.getRequests().get(0).getHeaders().get(0).getDisplay()).isEqualTo("abcd-efab-1234-5678");
        assertThat(post.getRequests().get(0).getHeaders().get(0).getExample()).isEqualTo("abcd-efab-1234-5678");

        JSONAssert.assertEquals(fixture("apispecs-test/v08/examples/book-post-request.json"), post.getRequests().get(0).getExample(), false);
        JSONAssert.assertEquals(fixture("apispecs-test/v08/schemas/book-request.json"), post.getRequests().get(0).getSchema(), false);

        // Test parameter information
        assertThat(post.getParameters()).hasSize(1);
        assertThat(post.getParameters().get(0).getName()).isEqualTo("x-call-id");

        // Test response information
        assertThat(post.getResponses()).hasSize(2);

        RamlResponseModel response = post.getResponses()
                .stream()
                .filter(forCodeAndContentType(201, MediaType.APPLICATION_JSON_TYPE))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No response found for [201, application/json]"));

        assertThat(response.getDescription()).isEqualTo("<p>book successfully added to collection</p>\n");
        assertThat(response.getCode()).isEqualTo(201);
        assertThat(MediaType.APPLICATION_JSON_TYPE.isCompatible(MediaType.valueOf(response.getContentType()))).isTrue();
        assertThat(response.isJson()).isTrue();

        JSONAssert.assertEquals(fixture("apispecs-test/v08/examples/book-response.json"), response.getExample(), false);
        JSONAssert.assertEquals(fixture("apispecs-test/v08/schemas/book-response.json"), response.getSchema(), false);
    }

    @Test
    public void shouldLoadGetActionInformationForList() {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources).hasSize(1);

        assertThat(resources.get(0).getUri()).isEqualTo("/books");

        List<RamlMethodModel> actions = resources.get(0).getActions();

        assertThat(actions).hasSize(2);

        RamlMethodModel get = actions.stream().filter(forType("GET"))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No [GET] action found on resource"));

        // Test basic action information
        assertThat(get.getType()).isEqualToIgnoringCase("GET");
        assertThat(get.getDescription()).isEqualTo("<p>return a list of books</p>\n");
        assertThat(get.isDeprecated()).isFalse();

        // Test security information
        assertThat(get.getSecurity()).hasSize(1);
        assertThat(get.getSecurity().get(0).getQueryParameters()).hasSize(1);
        assertThat(get.getSecurity().get(0).getQueryParameters().get(0).getParameterType()).isEqualTo("query");
        assertThat(get.getSecurity().get(0).getQueryParameters().get(0).getName()).isEqualTo("custom-sec");

        // Test request information
        assertThat(get.getRequests()).isEmpty();

        // Test parameter information
        assertThat(get.getParameters()).hasSize(1);
        assertThat(get.getParameters().get(0).getName()).isEqualTo("genre");
        assertThat(get.getParameters().get(0).getDescription()).isEqualTo("<p>the genre to filter on</p>\n");
        assertThat(get.getParameters().get(0).getDataType()).isEqualTo("string");
        assertThat(get.getParameters().get(0).getFlags()).isEqualTo("optional");
        assertThat(get.getParameters().get(0).getAllowedValues()).hasSize(6);
        assertThat(get.getParameters().get(0).getAllowedValues()).contains("War", "Crime", "Sci-Fi", "Romance", "Comedy", "Fantasy");
        assertThat(get.getParameters().get(0).getPattern()).isNull();
        assertThat(get.getParameters().get(0).getExample()).isEqualTo("War");
        assertThat(get.getParameters().get(0).getDisplay()).isEqualTo("War");
        assertThat(get.getParameters().get(0).getDefault()).isEqualTo("War");
    }

    @Test
    public void shouldLoadHeaderInformationFromResponse() {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources).hasSize(1);

        List<RamlResourceModel> children = resources.get(0).getResources();

        assertThat(children).hasSize(1);

        assertThat(children.get(0).getUri()).isEqualTo("/books/{id}");

        RamlResponseModel response = children.get(0)
                .getActions()
                .stream().filter(forType("GET"))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find [GET] action"))
                .getResponses()
                .stream().filter(forCodeAndContentType(200, MediaType.APPLICATION_JSON_TYPE))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No response found for [200, application/json]"));

        assertThat(response.getHeaders()).hasSize(1);
        assertThat(response.getHeaders().get(0).getName()).isEqualTo("x-call-id");
        assertThat(response.getHeaders().get(0).getDisplay()).isEqualTo("abcd-efab-1234-5678");
    }

    private void verifyBasicSecurity(RamlSecurityModel model) {
        assertThat(model.getType()).isEqualTo("Basic Authentication");
        assertThat(model.getHeaders()).hasSize(1);
        assertThat(model.getHeaders().get(0).getName()).isEqualTo("Authorization");
        assertThat(model.getHeaders().get(0).getDisplay()).isEqualTo("Basic (.*)");
        assertThat(model.getHeaders().get(0).getPattern()).isEqualTo("Basic (.*)");
        assertThat(model.getResponses()).hasSize(1);
        assertThat(model.getResponses().get("401")).isEqualTo("Invalid username or password provided\n");
    }

    private static Predicate<RamlMethodModel> forType(final String actionType) {
        return model -> model.getType().equalsIgnoreCase(actionType);
    }

    private static Predicate<RamlResponseModel> forCodeAndContentType(final Integer code, final MediaType contentType) {
        return model1 -> model1.getCode().equals(code) && contentType.isCompatible(MediaType.valueOf(model1.getContentType()));
    }
}
