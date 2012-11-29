package no.spp.examples.clientlogin.web.pages.homepage;

import no.spp.examples.clientlogin.web.panels.login.LoginPanel;
import no.spp.examples.clientlogin.web.panels.logout.LogoutPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        add(new LoginPanel("loginPanel"));
        add(new LogoutPanel("logoutPanel"));
    }
}
