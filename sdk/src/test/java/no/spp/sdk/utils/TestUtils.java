package no.spp.sdk.utils;

import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.SPPClientResponse;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestUtils {

	public SPPClientResponse assertHttpCodeForRequest(SPPClient client, String endpoint) {
		return assertHttpCodeForRequest(client, endpoint, new HashMap<String, String>(0));
	}

	public SPPClientResponse assertHttpCodeForRequest(SPPClient client, String endpoint, Map<String, String> params) {
		SPPClientResponse response = null;
		try {
			response = client.GET(endpoint, params);
			Integer httpResponseCode = response.getHttpResponseCode();
			
			Assert.assertEquals(200, (int)httpResponseCode);
		} catch (SPPClientResponseException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (SPPClientRefreshTokenException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (SPPClientException e) {
			e.printStackTrace();
			Assert.fail();
		}
		return response;
	}

	public Properties getLoadedProperties() throws IOException {
		Properties properties = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
		properties.load(is);
		return properties;
	}

}
