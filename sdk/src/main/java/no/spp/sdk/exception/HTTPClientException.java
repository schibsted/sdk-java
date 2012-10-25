package no.spp.sdk.exception;

public class HTTPClientException extends Exception 
{
	public HTTPClientException(String message) 
	{
		super(message);
	}
	
	public HTTPClientException(Throwable throwable)
	{
		super(throwable);
	}
	
	public HTTPClientException(String message, Throwable throwable) 
	{
		super(message, throwable);
	}	
}
