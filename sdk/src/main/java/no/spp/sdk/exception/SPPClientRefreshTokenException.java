package no.spp.sdk.exception;
/**
 * This exception is thrown if an SPPClient is try to refresh an expired server-server or user access token.
 * 
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 *
 */
public class SPPClientRefreshTokenException extends Exception
{
	public SPPClientRefreshTokenException(String message) 
	{
		super(message);
	}
	
	public SPPClientRefreshTokenException(Throwable throwable)
	{
		super(throwable);
	}
	
	public SPPClientRefreshTokenException(String message, Throwable throwable) 
	{
		super(message, throwable);
	}	
}
