package no.spp.sdk.client;

import no.spp.sdk.oauth.OauthCredentials;
import no.spp.sdk.oauth.OauthHelper;
import org.apache.log4j.Logger;

/**
 * This is a user client.
 *
 * @author jono
 */
public class SPPUserClient extends SPPClient {

    private Logger log = Logger.getLogger(SPPServerClient.class);

    SPPUserClient(OauthCredentials oauthCredentials, SppApi sppApi,
                  OauthHelper oauthHelper, SPPClientAPISecurity sppClientAPISecurity) {
        super(oauthCredentials, sppApi, oauthHelper, sppClientAPISecurity);
    }

    @Override
    protected void handleInvalidGrantException() {
        // There is nothing we could do, the user need to log in again
        log.debug("User client does not handle invalid grant exception. The user must log in again.");
    }
}
