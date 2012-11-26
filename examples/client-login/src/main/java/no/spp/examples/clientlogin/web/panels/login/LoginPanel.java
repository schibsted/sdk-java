package no.spp.examples.clientlogin.web.panels.login;

import no.spp.examples.clientlogin.ApplicationSettings;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.oauth.OauthHelper;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.markup.html.panel.Panel;

public class LoginPanel extends Panel {

    private static String getAuthorizationURL() {
        try {
            return OauthHelper.getAuthorizationURL(ApplicationSettings.CLIENT_ID, ApplicationSettings.REDIRECT_URI, ApplicationSettings.SERVER_URL);
        } catch (SPPClientException e) {
            System.out.println(e.getMessage());
            throw new RestartResponseAtInterceptPageException(InternalErrorPage.class);
        }
    }

    public LoginPanel(String id) {
        super(id);

        add(new ExternalLink("login", getAuthorizationURL()));
    }
}
