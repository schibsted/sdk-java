package no.spp.examples.clientlogin;

import no.spp.examples.clientlogin.web.pages.homepage.HomePage;
import no.spp.examples.clientlogin.web.pages.login.LoginPage;
import org.apache.wicket.markup.html.WebPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see com.mycompany.Start#main(String[])
 */
public class ClientLoginApplication extends org.apache.wicket.protocol.http.WebApplication
{    	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();

        mountPage("/explorer", LoginPage.class);
	}
}
