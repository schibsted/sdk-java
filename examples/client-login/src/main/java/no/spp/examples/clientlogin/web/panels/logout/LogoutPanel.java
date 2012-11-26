package no.spp.examples.clientlogin.web.panels.logout;

import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.SPPClientResponse;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.markup.html.panel.Panel;

public class LogoutPanel extends Panel {
    public LogoutPanel(String id, SPPClient client) {
        super(id);

        try {
            SPPClientResponse response = client.GET("/me");

            add(new Label("username", response.getJSONObject().get("displayName").toString()));
            add(new Label("userId", response.getJSONObject().get("userId").toString()));
            add(new Label("email", response.getJSONObject().get("email").toString()));

            add(new ExternalLink("logout", "http://www.google.no.spp.examples"));

        } catch (SPPClientException e) {
            System.out.println(e.getMessage());
            throw new RestartResponseAtInterceptPageException(new InternalErrorPage());
        } catch (SPPClientResponseException e) {
            System.out.println(e.getMessage());
            throw new RestartResponseAtInterceptPageException(new InternalErrorPage());
        } catch (SPPClientRefreshTokenException e) {
            System.out.println(e.getMessage());
            throw new RestartResponseAtInterceptPageException(new InternalErrorPage());
        }
    }
}
