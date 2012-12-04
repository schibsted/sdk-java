package no.spp.sdk.client;

import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.oauth.ClientCredentials;
import no.spp.sdk.oauth.OauthCredentials;
import no.spp.sdk.oauth.OauthHelper;

/**
 * This is the server client builder. Generic functionality is placed in parent.
 * Specific functionality is placed here.
 *
 * @author jono
 */
public class ServerClientBuilder extends SPPClientBuilder<SPPServerClient> {
    public ServerClientBuilder(ClientCredentials clientCredentials) {
        super(clientCredentials);
    }

    public OauthCredentials getInitialOauthCredentials(OauthHelper oauthHelper) throws SPPClientException {
        return oauthHelper.getServerAccessToken();
    }

    @Override
    protected SPPServerClient createClient(SppApi sppApi, OauthHelper oauthHelper, SPPClientAPISecurity sppClientAPISecurity)
            throws SPPClientException {
        return new SPPServerClient(getInitialOauthCredentials(oauthHelper), sppApi, oauthHelper, sppClientAPISecurity);
    }
}
