package no.spp.examples.api.web.pages.login;

import no.spp.examples.api.ApplicationSettings;
import no.spp.examples.api.SPiDWebSession;
import no.spp.examples.api.web.pages.homepage.HomePage;
import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.UserClientBuilder;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.oauth.ClientCredentials;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

public class LoginPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public LoginPage(final PageParameters parameters) {
        super(parameters);

        StringValue code = getRequest().getClientUrl().getQueryParameterValue("code");

        if (!code.isEmpty()) {
            try {
                ClientCredentials clientCredentials = new ClientCredentials(ApplicationSettings.CLIENT_ID, ApplicationSettings.CLIENT_SECRET, ApplicationSettings.REDIRECT_URI);
                SPPClient client = new UserClientBuilder(clientCredentials)
                        .withUserAuthorizationCode(code.toString())
                        .withBaseUrl(ApplicationSettings.SERVER_URL)
                        .withApiVersion(ApplicationSettings.API_VERSION)
                        .build();

                SPiDWebSession.get().setSppClient(client);
            } catch (SPPClientException e) {
                System.out.println(e.getMessage());
                throw new RestartResponseAtInterceptPageException(InternalErrorPage.class);
            }
        }

        // Clear parameters
        setResponsePage(new HomePage(new PageParameters()));
    }
}
