package no.spp.sdk.fake;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthClientResponse;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import no.spp.sdk.exception.HTTPClientException;
import no.spp.sdk.net.HTTPClient;
import no.spp.sdk.net.HTTPClientResponse;

import java.util.Map;

public class FakeHTTPClientWithFixedResponse implements HTTPClient{


    private String responseBody;

    public FakeHTTPClientWithFixedResponse(String responseBody) {
        this.responseBody = responseBody;
    }

    public HTTPClientResponse execute(String url, Map<String, String> parameters, Map<String, String> headers, String requestMethod) throws HTTPClientException {
        return new HTTPClientResponse(200, responseBody);
    }

    public <T extends OAuthClientResponse> T execute(OAuthClientRequest oAuthClientRequest, Map<String, String> stringStringMap, String s, Class<T> tClass) throws OAuthSystemException, OAuthProblemException {
        FakeClientResponse resp = new FakeClientResponse();
        resp.init(responseBody, "application/json", 200);
        return (T)resp;
    }

    public static class FakeClientResponse extends OAuthJSONAccessTokenResponse{

        @Override
        protected void init(String body, String contentType, int responseCode) throws OAuthProblemException {
            super.init(body, contentType, responseCode);
        }

    }
}
