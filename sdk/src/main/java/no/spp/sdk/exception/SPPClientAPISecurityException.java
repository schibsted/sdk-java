package no.spp.sdk.exception;

/**
 * @author Joakim Wånggren <joakim.wanggren@schibsted.se>
 */
public class SPPClientAPISecurityException extends SPPClientException {

    public SPPClientAPISecurityException(String message)
	{
		super(message);
	}

	public SPPClientAPISecurityException(Throwable throwable)
	{
		super(throwable);
	}

	public SPPClientAPISecurityException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
}
