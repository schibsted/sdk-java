/**
 * Ok, this class may not be needed. Seems like overkill right now...
 *
 */
package no.spp.sdk.client;

import no.spp.sdk.net.HTTPMethod;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {
    private String method;
    private HTTPMethod httpMethod = HTTPMethod.GET;
    private Map<String, String> parameters = new HashMap<String, String>();

    public RequestBuilder forEndpoint(String endpoint) {
        this.method = endpoint;
        return this;
    }

    public RequestBuilder usingHttpMethod(HTTPMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public RequestBuilder withParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public SPPClientRequest build() {
        return new SPPClientRequest(method, parameters, httpMethod);
    }

}
