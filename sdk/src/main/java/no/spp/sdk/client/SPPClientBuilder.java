package no.spp.sdk.client;

import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.net.HTTPClient;
import no.spp.sdk.net.URLConnectionClient;
import no.spp.sdk.oauth.ClientCredentials;
import no.spp.sdk.oauth.OauthCredentials;
import no.spp.sdk.oauth.OauthHelper;

/**
 * This is the parent for building SPP clients.
 * There are different clients so make sure to use the correct
 * builder implementation.
 */
public abstract class SPPClientBuilder<T extends SPPClient> {

    private ClientCredentials clientCredentials;
    protected OauthCredentials oauthCredentials;
    private String sppBaseUrl = null;

    // Fields initialized with sensible defaults
    private Boolean autoRefreshToken = true;
    private String apiVersion = "2";
    private HTTPClient httpClient = new URLConnectionClient();

    public SPPClientBuilder(ClientCredentials clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    /**
     * Mandatory base url.
     *
     * @param baseUrl e.g. https://payment.schibsted.se
     * @return The builder object
     */
    public SPPClientBuilder withBaseUrl(String baseUrl) {
        this.sppBaseUrl = baseUrl;
        return this;
    }

    /**
     * Override the default api version. Default = 2
     *
     * @param apiVersion API version to use
     * @return The builder object
     */
    public SPPClientBuilder withApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    /**
     * Override the default setting to autoRefresh access tokens. Default is true.
     *
     * @param autoRefreshToken Should autorefresh token
     * @return The builder object
     */
    public SPPClientBuilder autoRefreshToken(boolean autoRefreshToken) {
        this.autoRefreshToken = autoRefreshToken;
        return this;
    }

    /**
     * Override the default HTTPClient. Default is a {@link no.spp.sdk.net.URLConnectionClient}
     *
     * @param httpClient the httpClient implementation to use for any underlying HTTP calls.
     * @return The builder object
     */
    public SPPClientBuilder withHTTPClient(HTTPClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * Endmarker for the builder.
     *
     * @return The configured SPPClient object.
     * @throws no.spp.sdk.exception.SPPClientException
     *
     */
    public T build() throws SPPClientException {
        if (sppBaseUrl == null) {
            throw new IllegalStateException("sppBaseUrl must be set");
        }
        SppApi api = new SppApi(httpClient, apiVersion, sppBaseUrl);
        OauthHelper oauthHelper = new OauthHelper(sppBaseUrl, clientCredentials, httpClient);
        SPPClientAPISecurity sppClientAPISecurity = new SPPClientAPISecurity(clientCredentials);
        T userClient = createClient(api, oauthHelper, sppClientAPISecurity);
        userClient.setAutoRefreshToken(autoRefreshToken);
        return userClient;
    }

    /**
     * Factory method for the client implementation.
     *
     * @param api API version to use
     * @param oauthHelper The OauthHelper to use
     * @return The client
     * @throws SPPClientException
     */
    protected abstract T createClient(SppApi api, OauthHelper oauthHelper, SPPClientAPISecurity sppClientAPISecurity) throws SPPClientException;
}
