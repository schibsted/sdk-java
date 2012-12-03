package no.spp.examples.api;

import no.spp.examples.api.web.pages.homepage.HomePage;
import no.spp.examples.api.web.pages.login.LoginPage;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 *
 * @see no.spp.examples.api.Start#main(String[])
 */
public class ApiApplication extends org.apache.wicket.protocol.http.WebApplication {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public final Session newSession(Request request, Response response) {
        return new SPiDWebSession(request);
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        mountPage("/login", LoginPage.class);
    }
}
