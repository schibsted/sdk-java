package no.spp.sdk.client;

import no.spp.sdk.exception.HTTPClientException;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.net.HTTPClient;
import no.spp.sdk.net.HTTPClientResponse;
import no.spp.sdk.net.HTTPMethod;
import no.spp.sdk.net.URLConnectionClient;

import java.util.HashMap;
import java.util.Map;

public class SppApi {

    public static final String OAUTH_TOKEN_PARAM_NAME = "oauth_token";

    private String apiVersion;
    private RequestBuilder requestBuilder = new RequestBuilder();
    private HTTPClient httpClient = new URLConnectionClient();
    private String apiBaseUrl;

    /** Create an SppApi instance
     *
     * @param httpClient
     * @param apiVersion
     * @param baseUrl e g https://payment.schibsted.se
     */
    public SppApi(HTTPClient httpClient, String apiVersion, String baseUrl) {
        this.httpClient = httpClient;
        this.apiVersion = apiVersion;
        this.apiBaseUrl = ensureTrailingSlash(baseUrl) + "api/" + apiVersion;
    }

    /** Create an SppApi instance
     *
     * @param apiVersion
     * @param baseUrl e g https://payment.schibsted.se
     */
    public SppApi( String apiVersion, String baseUrl) {
        this.apiVersion = apiVersion;
        this.apiBaseUrl = ensureTrailingSlash(baseUrl) + "api/" + apiVersion;
    }

    public SPPClientResponse makeRequest(SPPClientRequest sppClientRequest, String oauthToken) throws SPPClientException, SPPClientResponseException {
        HTTPClientResponse response;
        Map<String, String> parameters = new HashMap<String, String>(sppClientRequest.getParameters()); //Create new map in case provided map is immutable
        parameters.put(OAUTH_TOKEN_PARAM_NAME, oauthToken);

        long startTime = System.currentTimeMillis();
        try {
            response = this.httpClient.execute(this.buildRequestURL(sppClientRequest), parameters, null, sppClientRequest.getHttpMethod().toString());

        } catch (HTTPClientException e) {
            throw new SPPClientException(e);
        }
        long endTime = System.currentTimeMillis();
        return new SPPClientResponse(response.getResponseCode(), response.getResponseBody(), this.apiVersion, endTime - startTime);

    }

    private String buildRequestURL(SPPClientRequest request) {
        return apiBaseUrl + ensureLeadingSlash(request.getEndpoint());
    }

    private String ensureLeadingSlash(String string) {
        return (string.startsWith("/") ? "" : "/") + string;
    }
    private String ensureTrailingSlash(String in) {
        return in + (in.endsWith("/") ? "" : "/" );
    }

    /**
     * Make an API GET request using the provided Oauthcredentials.
     *
     * @param endpoint the API method to call, e.g "me", "user", "user/123"
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse GET(String endpoint, String accessToken) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return this.makeRequest(requestBuilder
                .forEndpoint(endpoint)
                .build(), accessToken);
    }

    /**
     * Make an API GET request using the Oauthcredentials of the SPPClient.
     *
     * @param endpoint     the API endpoint to call, e.g "me", "user", "user/123"
     * @param parameters API parameters
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse GET(String endpoint, Map<String, String> parameters, String accessToken) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return this.makeRequest(requestBuilder.forEndpoint(endpoint).withParameters(parameters).build(), accessToken);
    }

    /**
     * Make an API POST request using the Oauthcredentials of the SPPClient.
     *
     * @param endPoint     the API method to call, e.g "me", "user", "user/123"
     * @param parameters API parameters
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse POST(String endPoint, Map<String, String> parameters, String accessToken) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return this.makeRequest(requestBuilder
                .forEndpoint(endPoint)
                .withParameters(parameters)
                .usingHttpMethod(HTTPMethod.POST)
                .build(), accessToken);
    }

    /**
     * Make an API PUT request using the Oauthcredentials of the SPPClient.
     *
     * @param endpoint     the API method to call, e.g "me", "user", "user/123"
     * @param parameters API parameters
     * @return API response object
     * @throws no.spp.sdk.exception.SPPClientException
     *
     * @throws no.spp.sdk.exception.SPPClientResponseException
     *
     * @throws no.spp.sdk.exception.SPPClientRefreshTokenException
     *          this will only be thrown if autoRefreshToken is set to true and it fails to refresh the token if it has expired.
     */
    public SPPClientResponse PUT(String endpoint, Map<String, String> parameters, String accessToken) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return this.makeRequest(requestBuilder
                .forEndpoint(endpoint)
                .withParameters(parameters)
                .usingHttpMethod(HTTPMethod.PUT)
                .build(), accessToken);
    }

    /**
     * Make an API DELETE request using the Oauthcredentials of the SPPClient.
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
    public SPPClientResponse DELETE(String method, String accessToken) throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
        return this.makeRequest(requestBuilder
                .forEndpoint(method)
                .usingHttpMethod(HTTPMethod.DELETE)
                .build(), accessToken);
    }

    /**
     * Set the API version prefix to use for API calls.
     *
     * @param apiVersion can be null for default API version
     */
    public void setAPIVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * The API version prefix to use for API calls.
     * It doesn't use any prefix by default.
     *
     * @return the API version
     */
    public String getAPIVersion() {
        return this.apiVersion;
    }

     /**
     * Get HTTPClient
     * if the client is null a default URLConnectionClient will be created, set and returned.
     *
     * @return the current HTTPClient
     */
    public HTTPClient getHTTPClient() {
        return this.httpClient;
    }

    /**
     * Set a custom HTTPClient to use for any underlying HTTP calls.
     *
     * @param httpClient the httpClient implementation to use for any underlying HTTP calls.
     */
    public void setHTTPClient(HTTPClient httpClient) {
        this.httpClient = httpClient;
    }
}
