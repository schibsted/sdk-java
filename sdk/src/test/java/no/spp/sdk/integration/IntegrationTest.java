package no.spp.sdk.integration;

import net.sf.json.JSONObject;
import no.spp.sdk.client.*;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.oauth.ClientCredentials;

import org.junit.Test;


public class IntegrationTest {

    public static void main(String[] args) throws SPPClientRefreshTokenException, SPPClientResponseException, SPPClientException {
        if (args.length == 0) {
            System.out.println("Please provide client secret as arg");
            System.exit(0);
        }
        String clientSecret = args[0];

        //Example use:
        String clientIdPlus = "4e8463569caf7ca019000007";
        String redirectUri = "http://aftonbladet.se/oauth/login";
        String endPoint = "products/";// +clientIdPlus;
        //String endPoint = "user/erik.fried@schibsted.se";
        //String endPoint = "user/100010/subscriptions";
        //String endPoint = "product/100005";
        //String endPoint = "user/100010/product/100006";
        String sppBaseUrl = "https://payment.schibsted.se";

        ClientCredentials credentials = new ClientCredentials(clientIdPlus, clientSecret, redirectUri);
        SPPClient serverClient = new ServerClientBuilder(credentials)
                .withBaseUrl(sppBaseUrl)
                .build();

        SPPClientResponse response = serverClient.GET(endPoint);
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
