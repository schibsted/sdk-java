package no.spp.sdk.client;

import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.oauth.ClientCredentials;
import no.spp.sdk.oauth.OauthCredentials;
import no.spp.sdk.oauth.OauthHelper;

/**
 * The builder for a user client. 
 * @author jono
 *
 */
public class UserClientBuilder extends SPPClientBuilder<SPPUserClient> {
    private String authorizationCode;

    public UserClientBuilder(ClientCredentials clientCredentials) {
        super(clientCredentials);
    }

     /** Conditionally mandatory property. either this one or {@link #withUserAuthorizationCode(String) }  must be invoked.
     *
     * @param oAuthAccessTokenResponse
     * @return
     */
    public UserClientBuilder withUserOauthCredentials(OauthCredentials oAuthAccessTokenResponse) {
        this.oauthCredentials = oAuthAccessTokenResponse;
        return this;
    }

    /** Conditionally mandatory property. either this one or {@link #withUserOauthCredentials(no.spp.sdk.oauth.OauthCredentials)}  must be invoked.
     *
     * <b>NOTE:</b> Set this property cautiously, typically only when a user have just authorized.
     * Will cause the build phase to be expensive, due to an extra http request exchanging the code for a token.
     *
     * @param authorizationCode
     * @return
     */
    public UserClientBuilder withUserAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
        return this;
    }

    public OauthCredentials getInitialOauthCredentials(OauthHelper oauthHelper) throws SPPClientException {
        if (oauthCredentials == null) {
            if (authorizationCode != null) {
                oauthCredentials = oauthHelper.getUserAccessToken( authorizationCode);
            } else {
                throw new IllegalStateException("Either oauthCredentials or authorizationCode must be set!");
            }
        }
        return oauthCredentials;
    }

	@Override
	protected SPPUserClient createClient(SppApi api, OauthHelper oauthHelper, SPPClientAPISecurity sppClientAPISecurity) throws SPPClientException {
		return new SPPUserClient(getInitialOauthCredentials(oauthHelper), api, oauthHelper, sppClientAPISecurity);
	}
}
