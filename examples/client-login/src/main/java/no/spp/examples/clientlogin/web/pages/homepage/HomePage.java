package no.spp.examples.clientlogin.web.pages.homepage;

import no.spp.examples.clientlogin.ApplicationSettings;
import no.spp.examples.clientlogin.SPiDWebSession;
import no.spp.examples.clientlogin.web.panels.login.LoginPanel;
import no.spp.examples.clientlogin.web.panels.logout.LogoutPanel;
import no.spp.sdk.client.SPPClient;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);

        //((MySession)Session.get()).setMyObject(myObject);
        // or java >= 1.5:


        // obtain sppclient from session
        SPPClient client = SPiDWebSession.get().getSppClient();//getSession().getMetaData(ApplicationSettings.SPP_CLIENT_META_DATA_KEY);

        // if we have a client we are logged
        if (client == null) {
            add(new LoginPanel("panel"));
        } else {
            add(new LogoutPanel("panel", client));
        }
    }
}
