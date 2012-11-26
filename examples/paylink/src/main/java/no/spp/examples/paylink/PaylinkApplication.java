package no.spp.examples.paylink;

import no.spp.examples.paylink.web.pages.homepage.HomePage;
import no.spp.examples.paylink.web.pages.paylink.PaylinkPage;
import org.apache.wicket.markup.html.WebPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 *
 * @see no.spp.examples.paylink.Start#main(String[])
 */
public class PaylinkApplication extends org.apache.wicket.protocol.http.WebApplication {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        mountPage("/paylink", PaylinkPage.class);
    }
}
