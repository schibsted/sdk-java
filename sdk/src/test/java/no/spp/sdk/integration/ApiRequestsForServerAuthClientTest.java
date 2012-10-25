package no.spp.sdk.integration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.SPPClientResponse;
import no.spp.sdk.client.ServerClientBuilder;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.net.HTTPClient;
import no.spp.sdk.net.URLConnectionClient;
import no.spp.sdk.oauth.ClientCredentials;
import no.spp.sdk.utils.TestUtils;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ApiRequestsForServerAuthClientTest {

	private SPPClient client;
	private TestUtils testUtils;
	private Properties properties;

	@Before
	public void setUp() throws SPPClientException, FileNotFoundException, IOException {
		testUtils = new TestUtils();
		properties = testUtils.getLoadedProperties();
		
		String clientId = properties.getProperty("clientId");
		String clientSecret = properties.getProperty("clientSecret");
		String redirectURI = properties.getProperty("redirectURI");
		HTTPClient httpClient = new URLConnectionClient();
		String sppBaseUrl = properties.getProperty("sppBaseUrl");
		String apiVersion = properties.getProperty("apiVersion");

        ClientCredentials clientCredentials = new ClientCredentials(clientId, clientSecret, redirectURI);

        client = new ServerClientBuilder(clientCredentials)
            .withBaseUrl(sppBaseUrl)
            .withHTTPClient(httpClient)
            .withApiVersion(apiVersion)
            .build();

	}

    @Test
	public void testGetEndpoints() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, "endpoints");
	}

	@Test
	public void testGetUserById() throws SPPClientException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId", properties.getProperty("getUserId"));
		testUtils.assertHttpCodeForRequest(client, "users", params);
	}

	@Test
	public void testGetUserByEmail() throws SPPClientException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", properties.getProperty("getUserEmail"));
		testUtils.assertHttpCodeForRequest(client, "users", params);
	}

	@Test
	public void testGetSubscriptionsByUserId() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, String.format("user/%s/subscriptions", properties.getProperty("getUserId")));
	}

	@Ignore("Give rights to the client to access endpoint")
	@Test
	public void testGetReport() throws SPPClientException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("from", properties.getProperty("from"));
		params.put("to", properties.getProperty("to"));
		testUtils.assertHttpCodeForRequest(client, "reports/template/1", params);
	}

	@Test
	public void testGetOrder() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, String.format("user/%s/orders", properties.getProperty("getUserId")));
	}

	@Test
	public void testGetDumps() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, "reports/dumps");
	}

	@Test
	public void testGetOrderItems() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, String.format("order/%s/items", properties.getProperty("getOrderId")));
	}

	@Test
	public void testGetTransactions() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, String.format("order/%s/transactions", properties.getProperty("getOrderId")));
	}

	@Test
	public void testGetProducts() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, "products");
	}

	@Test
	public void testGetDiscount() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, String.format("discount/%s", properties.getProperty("getDiscount")));
	}

	@Test
	public void testGetVersion() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, "version");
	}

	@Test
	public void testGetLogins() throws SPPClientException {
		testUtils.assertHttpCodeForRequest(client, String.format("logins/%s", properties.getProperty("getUserId")));
	}
}
