package no.spp.sdk.net;

/**
 * HTTPClientResponse holds information about a HTTP response
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 *
 */
public class HTTPClientResponse 
{
	private Integer responseCode;
	private String responseBody;
	
	/**
	 * Create a instance of HTTPClientResponse
	 * @param responseCode the HTTP response code from an HTTP request 
	 * @param responseBody the HTTP response body from an HTTP request
	 */
	public HTTPClientResponse(Integer responseCode, String responseBody) 
	{
		this.responseCode = responseCode;
		this.responseBody = responseBody;
	}

	/**
	 * Get the response code
	 * 
	 * @return the response code
	 */
	public Integer getResponseCode() 
	{
		return responseCode;
	}

	/**
	 * Get the response body
	 * 
	 * @return the response body
	 */
	public String getResponseBody() 
	{
		return responseBody;
	}
	
}
