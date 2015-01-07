package net.ozwolf.raml.model;

import org.junit.BeforeClass;
import org.junit.Test;
import org.raml.model.ActionType;
import org.raml.model.Protocol;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static net.ozwolf.raml.configuration.ApiSpecsConfiguration.configuration;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ModelTest {
    private static RamlModel model;

    @BeforeClass
    public static void setUp() {
        ModelTest.model = new RamlModel("apispecs-test/apispecs.raml", configuration());
    }

    @Test
    public void shouldLoadRamlAndExposeModelInformationForView() {
        assertThat(model.getTitle(), is("Test API"));
        assertThat(model.getVersion(), is("1.0-SNAPSHOT"));
        assertThat(model.getProtocols(), hasItem(Protocol.HTTP));
    }

    @Test
    public void shouldLoadResourcesAsModels() {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources.size(), is(1));

        assertThat(resources.get(0).getId(), is("2f626f6f6b73"));
        assertThat(resources.get(0).getDisplayName(), is("Books"));
        assertThat(resources.get(0).getDescription(), is("<p>resource for getting and managing <code>books</code></p>\n"));
        assertThat(resources.get(0).getUri(), is("/books"));

        assertThat(resources.get(0).getActions().size(), is(2));

        assertThat(resources.get(0).getResources().size(), is(1));

        assertThat(resources.get(0).getResources().get(0).getId(), is("2f626f6f6b732f7b69647d"));
        assertThat(resources.get(0).getResources().get(0).getActions().size(), is(1));
    }

    @Test
    public void shouldLoadDocumentation() {
        List<RamlDocumentationModel> documentation = model.getDocumentation();

        assertThat(documentation.size(), is(1));

        assertThat(documentation.get(0).getId(), is("53756d6d617279"));
        assertThat(documentation.get(0).getTitle(), is("Summary"));
        assertThat(documentation.get(0).getContent(), is("<h2>Summary</h2>\n<p>This API specification is for a test purposes only.  This should use <code>markdown4j</code> to parse the Markdown.</p>\n"));
    }

    @Test
    public void shouldLoadPostActionInformation() throws IOException {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources.size(), is(1));

        assertThat(resources.get(0).getUri(), is("/books"));

        List<RamlActionModel> actions = resources.get(0).getActions();

        assertThat(actions.size(), is(2));

        RamlActionModel post = actions.stream().filter(forType(ActionType.POST)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No [POST] action found on resource"));

        // Test basic action information
        assertThat(post.getType(), is("POST"));
        assertThat(post.getDescription(), is("<p>add a book to the collection</p>\n"));
        assertThat(post.isDeprecated(), is(true));

        // Test security information
        assertThat(post.getSecurity().size(), is(1));
        verifyBasicSecurity(post.getSecurity().get(0));

        // Test request information
        assertThat(post.getRequests().size(), is(1));
        assertThat(post.getRequests().get(0).getId(), is("6170706c69636174696f6e2f6a736f6e"));
        assertThat(post.getRequests().get(0).getContentType(), is("application/json"));
        assertThat(post.getRequests().get(0).isJson(), is(true));

        assertThat(post.getRequests().get(0).getHeaders().size(), is(1));
        assertThat(post.getRequests().get(0).getHeaders().get(0).getName(), is("x-call-id"));
        assertThat(post.getRequests().get(0).getHeaders().get(0).getDisplay(), is("abcd-efab-1234-5678"));
        assertThat(post.getRequests().get(0).getHeaders().get(0).getDisplay(), is("abcd-efab-1234-5678"));

        assertThat(post.getRequests().get(0).getExample(), is(fixture("apispecs-test/examples/book-post-request.json")));
        assertThat(post.getRequests().get(0).getSchema(), is(fixture("apispecs-test/schemas/book-request.json")));

        // Test parameter information
        assertThat(post.getParameters().size(), is(1));
        assertThat(post.getParameters().get(0).getName(), is("x-call-id"));

        // Test response information
        assertThat(post.getResponses().size(), is(2));

        RamlResponseModel response = post.getResponses()
                .stream()
                .filter(forCodeAndContentType(201, MediaType.APPLICATION_JSON))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No response found for [201, application/json]"));

        assertThat(response.getDescription(), is("<p>book successfully added to collection</p>\n"));
        assertThat(response.getCode(), is(201));
        assertThat(response.getContentType(), is(MediaType.APPLICATION_JSON));
        assertThat(response.isJson(), is(true));
        assertThat(response.getExample(), is(fixture("apispecs-test/examples/book-response.json")));
        assertThat(response.getSchema(), is(fixture("apispecs-test/schemas/book-response.json")));
    }

    @Test
    public void shouldLoadGetActionInformationForList() {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources.size(), is(1));

        assertThat(resources.get(0).getUri(), is("/books"));

        List<RamlActionModel> actions = resources.get(0).getActions();

        assertThat(actions.size(), is(2));

        RamlActionModel get = actions.stream().filter(forType(ActionType.GET))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No [GET] action found on resource"));

        // Test basic action information
        assertThat(get.getType(), is("GET"));
        assertThat(get.getDescription(), is("<p>return a list of books</p>\n"));
        assertThat(get.isDeprecated(), is(false));

        // Test security information
        assertThat(get.getSecurity().size(), is(1));
        assertThat(get.getSecurity().get(0).getQueryParameters().size(), is(1));
        assertThat(get.getSecurity().get(0).getQueryParameters().get(0).getName(), is("custom-sec"));

        // Test request information
        assertThat(get.getRequests(), empty());

        // Test parameter information
        assertThat(get.getParameters().size(), is(1));
        assertThat(get.getParameters().get(0).getName(), is("genre"));
        assertThat(get.getParameters().get(0).getDescription(), is("<p>the genre to filter on</p>\n"));
        assertThat(get.getParameters().get(0).getDataType(), is("string"));
        assertThat(get.getParameters().get(0).getFlags(), is("optional"));
        assertThat(get.getParameters().get(0).getAllowedValues().size(), is(6));
        assertThat(get.getParameters().get(0).getAllowedValues(), hasItems("War", "Crime", "Sci-Fi", "Romance", "Comedy", "Fantasy"));
        assertThat(get.getParameters().get(0).getPattern(), nullValue());
        assertThat(get.getParameters().get(0).getExample(), is("War"));
        assertThat(get.getParameters().get(0).getDisplay(), is("War"));
        assertThat(get.getParameters().get(0).getDefault(), is("War"));
    }

    @Test
    public void shouldLoadHeaderInformationFromResponse() {
        List<RamlResourceModel> resources = model.getResources();

        assertThat(resources.size(), is(1));

        List<RamlResourceModel> children = resources.get(0).getResources();

        assertThat(children.size(), is(1));

        assertThat(children.get(0).getUri(), is("/books/{id}"));

        RamlResponseModel response = children.get(0)
                .getActions()
                .stream().filter(forType(ActionType.GET))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Could not find [GET] action"))
                .getResponses()
                .stream().filter(forCodeAndContentType(200, MediaType.APPLICATION_JSON))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No response found for [200, application/json]"));

        assertThat(response.getHeaders().size(), is(1));
        assertThat(response.getHeaders().get(0).getName(), is("x-call-id"));
        assertThat(response.getHeaders().get(0).getDisplay(), is("abcd-efab-1234-5678"));
    }

    private void verifyBasicSecurity(RamlSecurityModel model) {
        assertThat(model.getType(), is("basic"));
        assertThat(model.getHeaders().size(), is(1));
        assertThat(model.getHeaders().get(0).getName(), is("Authorization"));
        assertThat(model.getHeaders().get(0).getDisplay(), is("Basic (.*)"));
        assertThat(model.getResponses().size(), is(1));
        assertThat(model.getResponses().get(401), is("Invalid username or password provided\n"));
    }

    private static Predicate<RamlActionModel> forType(final ActionType actionType) {
        return model -> model.getType().equals(actionType.name());
    }

    private static Predicate<RamlResponseModel> forCodeAndContentType(final Integer code, final String contentType) {
        return model1 -> model1.getCode().equals(code) && model1.getContentType().equals(contentType);
    }
}
