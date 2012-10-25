package no.spp.sdk.exception;

public class SPPClientResponseException extends Exception
{
	private Integer httpResponseCode;
	private String responseBody;	
	
	public SPPClientResponseException(String responseBody, Integer httpResponseCode) 
	{
		super();
		this.httpResponseCode = httpResponseCode;
		this.responseBody = responseBody;
	}

	public Integer getHttpResponseCode() 
	{
		return httpResponseCode;
	}

	public String getResponseBody() 
	{
		return responseBody;
	}

}
