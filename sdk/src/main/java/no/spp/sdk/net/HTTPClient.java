package no.spp.sdk.net;

import java.util.Map;

import no.spp.sdk.exception.HTTPClientException;

/**
 * HTTPClient interface
 * This interface should be implemented to be able change the underlying HTTP client SPP SDK uses.
 * 
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 *
 */
public interface HTTPClient extends net.smartam.leeloo.client.HttpClient {
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String REQUEST_METHOD_POST = "POST";

	/**
	 * Makes a HTTP request and returns a HTTPClientResponse containing response body and response code.
	 * 
	 * @param url
	 * @param parameters
	 * @param headers
	 * @param requestMethod e.g GET, POST, PUT, DELETE, HEAD
	 * @return HTTPClientResponse containing the the body and response code from the request. 
	 * @throws HTTPClientException if the request wasn't successful
	 */
    public HTTPClientResponse execute(String url, Map<String, String> parameters, Map<String, String> headers, String requestMethod) throws HTTPClientException;
}
