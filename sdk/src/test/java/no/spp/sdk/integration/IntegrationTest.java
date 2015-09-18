package no.spp.sdk.integration;

import net.sf.json.JSONObject;
import no.spp.sdk.client.*;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.oauth.ClientCredentials;

import org.junit.Test;
import java.util.HashMap;

public class IntegrationTest {

    @Test
    public void emptyTestToPreventFail() {
    }

    public static void main(String[] args) throws SPPClientRefreshTokenException, SPPClientResponseException, SPPClientException {
        if (args.length < 2) {
            System.out.println("Please provide client secret and client signature secret as arg");
            System.exit(0);
        }
        String clientSecret = args[0];
        String clientSignSecret = args[1];

        String redirectUri = "http://localhost:8080";
        String clientId = "<Client ID>";
        String sppBaseUrl = "https://identity-pre.schibsted.com";

        ClientCredentials credentials = new ClientCredentials(clientId, clientSecret, clientSignSecret, redirectUri);
        SPPClient serverClient = new ServerClientBuilder(credentials)
                .withBaseUrl(sppBaseUrl)
                .build();

        //===== A get example =====
        //String endPoint = "user/100010/product/100006";
        //SPPClientResponse response = serverClient.GET(endPoint);
        //===========

        //===== A post example =====
        String endPoint = "user/286662/charge";
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("requestReference", "213");
        parameters.put("items", "[{\"name\":\"Product 1\",\"price\":\"0\"},{\"name\":\"Product 2\",\"price\":\"0\"}]");

        // Create hash
        SPPClientAPISecurity clientSecurity = new SPPClientAPISecurity(credentials);
        parameters.put("hash", clientSecurity.generateHash( parameters));

        SPPClientResponse response = serverClient.POST(endPoint, parameters);
        //==========

        System.out.println("Request roundtrip time: " + response.getResponseTime());
        //System.out.println(response.getJsonContainer()("data"));
        JSONObject jsonContainer = response.getJsonContainer();

        JSONObject data = jsonContainer.getJSONObject("data");
        if (data.isArray()) {
            for (Object endpoint : jsonContainer.getJSONArray("data")) {
                System.out.println(endpoint);
            }
        } else {
            System.out.println(data);
        }
    }
}
