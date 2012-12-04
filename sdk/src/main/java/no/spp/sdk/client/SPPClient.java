package no.spp.sdk.client;


import no.spp.sdk.exception.*;
import no.spp.sdk.net.HTTPMethod;
import no.spp.sdk.oauth.ClientCredentials;
import no.spp.sdk.oauth.OauthCredentials;
import no.spp.sdk.oauth.OauthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * SPPClient is the class to use for communication with VG services api.
 * This should be instantiated by a {@link SPPClientBuilder}-implementation.
 *
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 * @see SPPClientBuilder
 */
public abstract class SPPClient {
    private static final Logger log = LoggerFactory.getLogger(SPPClient.class);

    // If Oauthcredentials are not immutable,
    // it has to be declared volatile to ensure double checked locking works properly upon refresh
    // see refreshAndGetOauthCredentials() method.
    private volatile OauthCredentials oauthCredentials;
    private final Object refreshOauthLock = new Object();
    private Boolean autoRefreshToken = true;

    private SppApi sppApi = null;
    protected OauthHelper oauthHelper;
    private SPPClientAPISecurity sppClientAPISecurity;

    SPPClient(OauthCredentials oauthCredentials, SppApi sppApi, OauthHelper oauthHelper, SPPClientAPISecurity sppClientAPISecurity) {
        this.oauthCredentials = oauthCredentials;
        this.sppClientAPISecurity = sppClientAPISecurity;
        this.sppApi = sppApi;
        this.oauthHelper = oauthHelper;
    }

    /**
     * Make an API GET call using the Oauthcredentials of the SPPClient.
     *
     * @param method the API method to call, e.g "me", "user", "user/123"
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse GET(String method) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return this.makeApiRequest(new RequestBuilder().forEndpoint(method).build());
    }

    /**
     * Make an API GET call using the Oauthcredentials of the SPPClient.
     *
     * @param method     the API method to call, e.g "me", "user", "user/123"
     * @param parameters API parameters
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse GET(String method, Map<String, String> parameters) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return makeApiRequest(new RequestBuilder().forEndpoint(method).withParameters(parameters).build());
    }

    /**
     * Make an API POST call using the Oauthcredentials of the SPPClient.
     *
     * @param method     the API method to call, e.g "me", "user", "user/123"
     * @param parameters API parameters
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse POST(String method, Map<String, String> parameters) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return makeApiRequest(new RequestBuilder()
                .forEndpoint(method)
                .withParameters(parameters)
                .usingHttpMethod(HTTPMethod.POST)
                .build());
    }

    /**
     * Make an API PUT call using the Oauthcredentials of the SPPClient.
     *
     * @param method     the API method to call, e.g "me", "user", "user/123"
     * @param parameters API parameters
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse PUT(String method, Map<String, String> parameters) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return makeApiRequest(new RequestBuilder()
                .forEndpoint(method)
                .withParameters(parameters)
                .usingHttpMethod(HTTPMethod.PUT)
                .build());
    }

    /**
     * Make an API DELETE call using the Oauthcredentials of the SPPClient.
     *
     * @param method the API method to call, e.g "me", "user", "user/123"
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse DELETE(String method) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return makeApiRequest(new RequestBuilder()
                .forEndpoint(method)
                .usingHttpMethod(HTTPMethod.DELETE)
                .build());
    }

    /**
     * Make an API call. The Oauthcredentials used to authorize the request are taken from the provided {@link SPPClientRequest}
     *
     * @param request API request object with information about the call you wish to make. You should ensure this object has valid and fresh oauth credentials
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse makeApiRequest(SPPClientRequest request) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        oauthCredentials = refreshAndGetOauthCredentials();
        SPPClientResponse response;
        try {
            response = sppApi.makeRequest(request, oauthCredentials.getAccessToken());
        } catch (SPPClientInvalidGrantException e) {
            log.debug("Received exception that we can handle", e);
            handleInvalidGrantException();
            // Try request again
            response = sppApi.makeRequest(request, oauthCredentials.getAccessToken());
        }
        if (response.isEncrypted()) {
            try {
                response = this.sppClientAPISecurity.validateAndDecodeSignedResponse(response);
            } catch (SPPClientAPISecurityException scase) {
                log.error(scase.getMessage());
                throw scase;
            }
        }

        return response;
    }

    /**
     * Handles a caught {@link SPPClientInvalidGrantException} during a request.
     *
     * @throws SPPClientException
     */
    protected abstract void handleInvalidGrantException() throws SPPClientException;

    /**
     * If true the SPPClient will try to autoRefresh its token if it has expired when doing api calls.
     *
     * @return true if it automatically will refresh expired access tokens
     */
    public Boolean autoRefreshToken() {
        return this.autoRefreshToken;
    }

    /**
     * Set whether the client should automatically refresh expired access tokens.
     *
     * @param autoRefresh true if the client should automatically refresh expired access tokens
     */
    public void setAutoRefreshToken(Boolean autoRefresh) {
        this.autoRefreshToken = autoRefresh;
    }

    /**
     * Get the Oauth Access Token this client is currently holding using.
     *
     * @return A <b>possibly expired</b> client Oauth Access Token
     */
    public OauthCredentials getOauthCredentials() {
        return this.oauthCredentials;
    }

    public ClientCredentials getClientCredentials() {
        return this.oauthHelper.getClientCredentials();
    }

    /**
     * Refreshes OauthCredentials if expired, then
     * get the Oauth Access Token this client is currently holding using
     *
     * @return non-expired oauthCredentials
     * @throws SPPClientRefreshTokenException
     * @throws SPPClientInvalidGrantException
     */
    private OauthCredentials refreshAndGetOauthCredentials() throws SPPClientRefreshTokenException, SPPClientInvalidGrantException {
        if (autoRefreshToken() && this.oauthCredentials.isExpiredNow()) {
            // Double checked locking. ( http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html )
            // Avoid unnecessary locking if credentials are not even expired.
            synchronized (refreshOauthLock) {
                // Now that we have lock - check that creds are still expired,
                // ie no other thread has updated creds while we were waiting for lock.
                if (this.oauthCredentials.isExpiredNow()) {
                    this.oauthCredentials = oauthHelper.refreshOauthCredentials(oauthCredentials);
                }
            }
        }
        return this.oauthCredentials;
    }

    protected void setOauthCredentials(OauthCredentials oauthCredentials) {
        this.oauthCredentials = oauthCredentials;
    }

    public SppApi getSppApi() {
        return sppApi;
    }
}
