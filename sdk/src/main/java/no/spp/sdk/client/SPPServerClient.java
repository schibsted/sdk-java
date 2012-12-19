package no.spp.sdk.client;

import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.oauth.OauthCredentials;
import no.spp.sdk.oauth.OauthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a server client. 
 * @author jono
 *
 */
public class SPPServerClient extends SPPClient {
	
	Logger log =  LoggerFactory.getLogger(SPPServerClient.class);
	
	SPPServerClient(OauthCredentials oauthCredentials, SppApi sppApi,
			OauthHelper oauthHelper, SPPClientAPISecurity sppClientAPISecurity) {
		super(oauthCredentials, sppApi, oauthHelper, sppClientAPISecurity);
	}

	/**
	 * Updates the server access token. 
	 */
	@Override
	protected void handleInvalidGrantException() throws SPPClientException {
    	log.info("Server client handles a invalid grant exception.");
		OauthCredentials serverAccessToken = oauthHelper.getServerAccessToken();
		this.setOauthCredentials(serverAccessToken);
	}
}
