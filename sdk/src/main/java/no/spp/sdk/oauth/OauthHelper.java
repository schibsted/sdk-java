package no.spp.sdk.oauth;

import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthAuthzResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientInvalidGrantException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.net.HTTPClient;
import no.spp.sdk.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class OauthHelper {

    public static final Logger log = LoggerFactory.getLogger(OauthHelper.class);
    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String OAUTH_TOKEN_PATH = "/oauth/token";
    public static final String INVALID_GRANT_ERROR = "invalid_grant";

    private String sppBaseUrl;
    private ClientCredentials clientCredentials;
    private final HTTPClient httpClient;

    public OauthHelper(String sppBaseUrl, ClientCredentials clientCredentials, HTTPClient httpClient) {
        this.sppBaseUrl = sppBaseUrl;
        this.clientCredentials = clientCredentials;
        this.httpClient = httpClient;
    }

    /**
     * Creates an authorization URL to use when authorizing a user.
     * Redirect the user to this URL, and the response from the redirect should be used with getAutenticatedUserClient();
     *
     * @param clientId    Your client ID.
     * @param redirectURL the URL that the user will be redirected to after signing up / register, the response on this url should be used with getAutenticatedUserClient()
     * @param sppBaseUrl Optional URL without trailing slash to override default URLs https://id.vgnett.no and https://login.vg.no (stage/production)
     *
     * @return The URL to use for authorization.
     * @throws no.spp.sdk.exception.SPPClientException if authorization URL creation fails
     */
    public static String getAuthorizationURL(String clientId, String redirectURL, String sppBaseUrl) throws SPPClientException {

        try {
            return OAuthClientRequest
                    .authorizationLocation(sppBaseUrl + "/oauth/authorize")
                    .setClientId(clientId)
                    .setRedirectURI(redirectURL)
                    .setParameter("response_type", "code")
                    .buildQueryMessage()
                    .getLocationUri();
        } catch (OAuthSystemException e) {
            log.error("Exception when getting authorization url for "+ clientId +" using redirect url: " +redirectURL +" and base url: " +sppBaseUrl );
            throw new SPPClientException(e);
        }
    }

    public static String getAuthorizationCode(HttpServletRequest request) throws SPPClientException {
        try {
            return OAuthAuthzResponse.oauthCodeAuthzResponse(request).getCode();
        } catch (OAuthProblemException e) {
            log.error("Exception when getting authorizationCode from request " +request.getQueryString());
            throw new SPPClientException(e);
        }
    }

    public OauthCredentials getServerAccessToken() throws SPPClientException {
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(sppBaseUrl + OAUTH_TOKEN_PATH)
                    .setParameter("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS)
                    .setClientId(clientCredentials.clientId)
                    .setClientSecret(clientCredentials.clientSecret)
                    .setRedirectURI(clientCredentials.redirectUri)
                    .buildBodyMessage();

            OAuthClient oAuthClient = new OAuthClient(httpClient);
            return new OauthCredentials(oAuthClient.accessToken(request));

        } catch (OAuthSystemException e) {
            log.error("Exception when getting server access token for base  url " + sppBaseUrl +" using " + clientCredentials );
            throw new SPPClientException(e);
        } catch (OAuthProblemException e) {
            log.error("Exception when getting server access token for base  url " + sppBaseUrl +" using " + clientCredentials );
    	    if (isInvalidGrantError(e)) 
    		    throw new SPPClientInvalidGrantException(e);
            
            throw new SPPClientException(e);
        }

    }

    /**
     * Use this if clientCredentials redirectUri is exactly the same as used in the authorization.
     *
     * @param authorizationCode code needed when requesting a user access token
     * @return OauthCredentials
     * @throws no.spp.sdk.exception.SPPClientException if OAuth exchange fails
     */
    public OauthCredentials getUserAccessToken(String authorizationCode) throws SPPClientException {
        return this.getUserAccessToken(authorizationCode, clientCredentials.getRedirectUri());
    }

    /**
     * @param authorizationCode code needed when requesting a user access token
     * @param redirectUri must be exactly the same as used in the authorization.
     * @return OauthCredentials
     * @throws no.spp.sdk.exception.SPPClientException if OAuth exchange fails
     */
    public OauthCredentials getUserAccessToken(String authorizationCode, String redirectUri) throws SPPClientException {
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation(sppBaseUrl + OAUTH_TOKEN_PATH)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(clientCredentials.getClientId())
                    .setClientSecret(clientCredentials.getClientSecret())
                    .setRedirectURI(redirectUri)
                    .setCode(authorizationCode)
                    .buildBodyMessage();

            OAuthClient oAuthClient = new OAuthClient(httpClient);

            log.debug("UseraccessToken request: {}{}",request.getLocationUri(), request.getBody());
            return new OauthCredentials(oAuthClient.accessToken(request));
        } catch (OAuthSystemException e) {
            log.error("Exception when getting user access token for base  url " + sppBaseUrl + " using " + clientCredentials );
            throw new SPPClientException(e);
        } catch (OAuthProblemException e) {
            log.error("Exception when getting user access token for base  url " + sppBaseUrl + " using " + clientCredentials );
    	    if (isInvalidGrantError(e)) 
    		    throw new SPPClientInvalidGrantException(e);
            throw new SPPClientException(e);
        }
    }

    /**
     * @param oauthCredentials the OauthCredentials to refresh
     * @return A new refreshed instance of {@link no.spp.sdk.oauth.OauthCredentials}
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException if OAuth exchange fails
     * @throws SPPClientInvalidGrantException if grant was found invalid when trying to refresh
     */
    public OauthCredentials refreshOauthCredentials(OauthCredentials oauthCredentials) throws SPPClientRefreshTokenException, SPPClientInvalidGrantException {
       try {
           Timer timer = new Timer();

           log.debug("Refreshing client token");
           OAuthClientRequest request = OAuthClientRequest
                   .tokenLocation(sppBaseUrl + "/oauth/token")
                   .setGrantType(GrantType.REFRESH_TOKEN)
                   .setClientId(clientCredentials.clientId)
                   .setClientSecret(clientCredentials.clientSecret)
                   .setRedirectURI(clientCredentials.redirectUri)
                   .setRefreshToken(oauthCredentials.getRefreshToken())
                   .buildBodyMessage();

           OAuthClient oAuthClient = new OAuthClient(httpClient);
           OauthCredentials refreshedOauthCredentials = new OauthCredentials(oAuthClient.accessToken(request));
           log.debug("Refresh done. Took: " + timer.getElapsedTime() + " millis");
           return refreshedOauthCredentials;
       } catch (OAuthSystemException e) {
           throw new SPPClientRefreshTokenException("Unable to refresh token", e);
       } catch (OAuthProblemException e) {
           if (isInvalidGrantError(e))
               throw new SPPClientInvalidGrantException(e);
           throw new SPPClientRefreshTokenException("Unable to refresh token", e);
       }
    }

    private boolean isInvalidGrantError(OAuthProblemException e) {
    	return e.getError() != null && e.getError().equals(INVALID_GRANT_ERROR);    	
    }
    public ClientCredentials getClientCredentials() {
        return clientCredentials;
    }
}
