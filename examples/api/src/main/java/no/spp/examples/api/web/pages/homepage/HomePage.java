package no.spp.examples.api.web.pages.homepage;

import no.spp.examples.api.web.panels.api.APIPanel;
import no.spp.examples.api.web.panels.login.LoginPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        add(new LoginPanel("loginPanel"));
        add(new APIPanel("apiPanel"));
    }
}
