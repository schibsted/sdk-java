package no.spp.sdk.client;

import net.sf.json.*;
import no.spp.sdk.exception.SPPClientResponseException;

/**
 * The response object for an API request
 *
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 */
public class SPPClientResponse {
    private String responseBody;
    private JSON json;
    private JSONObject jsonContainer;
    private Double apiVersion;
    private Integer httpResponseCode;
    private final long responseTime;

    /**
     * Create SPPClientResponse instance
     *
     * @param httpResponseCode HTTP response code
     * @param responseBody     HTTP response body
     * @param apiVersion       version of api returning this response
     * @param responseTime     round trip time of api request
     * @throws SPPClientResponseException if response code is an error
     */
    public SPPClientResponse(Integer httpResponseCode, String responseBody, String apiVersion, long responseTime) throws SPPClientResponseException {
        this.validate(httpResponseCode, responseBody);
        this.httpResponseCode = httpResponseCode;
        this.responseBody = responseBody;
        this.apiVersion = Double.valueOf(apiVersion);
        this.responseTime = responseTime;
        initJsonContainer();
    }

    /**
     * Get JSON object for this response body
     *
     * @return the JSON object for this response body
     */
    public JSONObject getJSONObject() {
        initJson();
        if (this.json.isArray()) {
            //this is for backwards compability
            throw new JSONException("A JSONObject text must begin with {");
        }
        return (JSONObject) this.json;
    }

    /**
     * Gets the response container.
     * Only for apiversion >= 2.
     *
     * @return The response as a JSONObject
     */
    public JSONObject getJsonContainer() {
        return this.jsonContainer;
    }

    /**
     * Get JSON array for this response body
     *
     * @return the JSON array for this response body
     */
    public JSONArray getJSONArray() {
        initJson();
        if (!this.json.isArray()) {
            //this is for backwards compability
            throw new JSONException("A JSONArray text must begin with [");
        }
        return (JSONArray) this.json;
    }

    /**
     * Get JSON for this response body
     *
     * @return the JSON for this response body
     */
    public JSON getJSON() {
        initJson();
        return this.json;
    }

    /**
     * Get the response body
     *
     * @return the response body
     */
    public String getResponseBody() {
        return this.responseBody;
    }

    void updateDataAfterDecryption(String data) {
        this.jsonContainer.element("data", data);
        this.jsonContainer.discard("sig");
        this.jsonContainer.discard("algorithm");
    }

    /**
     * Gets the values of "data" in the container for apiversion >= 2.
     * Gets the entire response for apiversion >= 1.
     *
     * @return Response as a String
     */
    public String getData() {
        if (apiVersion >= 2) {
            return this.jsonContainer.getString("data");
        } else {
            return this.responseBody;
        }
    }

    /**
     * Only for apiversion >= 2.
     *
     * @return Debug container
     */
    public String getDebug() {
        if (!isSecondVersionOrHigher()) return null;
        return getValueOrNull(this.jsonContainer, "debug");
    }

    /**
     * Only for apiversion >= 2.
     *
     * @return Error container
     */
    public String getError() {
        if (!isSecondVersionOrHigher()) return null;
        return getValueOrNull(this.jsonContainer, "error");
    }

    public Integer getHttpResponseCode() {
        return this.httpResponseCode;
    }

    /**
     * Only for apiversion >= 2.
     *
     * @return Meta container
     */
    public String getMeta() {
        if (!isSecondVersionOrHigher()) return null;
        return getValueOrNull(this.jsonContainer, "meta");
    }

    /**
     * Only for apiversion >= 2.
     *
     * @return Request container
     */
    public String getRequestMeta() {
        if (!isSecondVersionOrHigher()) return null;
        return getValueOrNull(this.jsonContainer, "request");
    }

    /**
     * Only for apiversion >= 2.
     *
     * @return Container type
     */
    public String getContainerType() {
        if (!isSecondVersionOrHigher()) return null;
        return getValueOrNull(this.jsonContainer, "type");
    }

    public String getResponseSignture() {
        if (!isSecondVersionOrHigher()) return null;
        return getValueOrNull(this.jsonContainer, "sig");
    }

    public String getResponseAlgorithm() {
        if (!isSecondVersionOrHigher()) return null;
        return getValueOrNull(this.jsonContainer, "algorithm");
    }

    public long getResponseTime() {
        return this.responseTime;
    }

    public boolean isEncrypted() {
        if (!isSecondVersionOrHigher()) return false;
        String signature = getResponseSignture();
        if (signature == null) return false;
        String algorithm = getResponseAlgorithm();
        return algorithm != null;
    }

    private String getValueOrNull(JSONObject object, String key) {
        if (object.containsKey(key))
            return object.getString(key);

        return null;
    }

    private boolean isSecondVersionOrHigher() {
        return this.apiVersion >= 2;
    }

    /**
     * Validate this response
     */
    private void validate(Integer httpResponseCode, String responseBody) throws SPPClientResponseException {
        if (httpResponseCode < 200 || httpResponseCode > 299) {
            throw new SPPClientResponseException(responseBody, httpResponseCode);
        }
    }

    private void initJsonContainer() {
        if (isSecondVersionOrHigher())
            this.jsonContainer = JSONObject.fromObject(this.getResponseBody());
    }

    private void initJson() {
        if (this.json != null) return;

        if (isSecondVersionOrHigher()) {
            this.json = JSONSerializer.toJSON(this.jsonContainer.getString("data"));
        } else {
            this.json = JSONSerializer.toJSON(this.responseBody);
        }
    }

    @Override
    public String toString() {
        return "SPPClientResponse{\n" +
                " responseBody='" + responseBody + '\'' +
                ",\n json=" + json +
                ",\n jsonContainer=" + jsonContainer +
                ",\n apiVersion=" + apiVersion +
                ", httpResponseCode=" + httpResponseCode +
                ", responseTime=" + responseTime +
                '}';
    }
}
