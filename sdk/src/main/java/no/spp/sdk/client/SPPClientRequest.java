package no.spp.sdk.client;

import no.spp.sdk.net.HTTPMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Request data to make API calls.
 * 
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 *
 */
public class SPPClientRequest
{
	private String endpoint;
	private HTTPMethod httpMethod;

	private Map<String,String> parameters = new HashMap<String, String>();

    /**
	 * Create SPPClientRequest instance with Builder
	 * 
	 * @param endpoint the API endpoint to call, e.g "me", "user", "user/123"
	 * @param parameters custom parameters for the API endpoint
	 * @param httpMethod the HTTP request endpoint to use for this request
	 */
     public SPPClientRequest(String endpoint, Map<String, String> parameters, HTTPMethod httpMethod){
        this.endpoint = replaceLeadingSlash(endpoint);
        if (parameters != null) {
            this.parameters.putAll(parameters); //Put all is because we modify the map later on.(adding oauth_token) -> We cause no side effects.
        }
        this.httpMethod = httpMethod;
    }

    private String replaceLeadingSlash(String method) {
        return (method.charAt(0) == '/') ? method.substring(1) : method;
    }

    /**
	 * Get the API endpoint name
	 * 
	 * @return API endpoint name
	 */
	public String getEndpoint()
	{
		return this.endpoint;
	}
	
	/**
	 * Add a parameter to this request.
	 * 
	 * @param key parameter name
	 * @param value parameter value
	 * @return this instance
	 */
	public SPPClientRequest addParameter(String key, String value) 
	{
		this.parameters.put(key, value);
		return this;
	}
	
	/**
	 * Get parameters
	 * 
	 * @return the parameters
	 */
	public Map<String, String> getParameters()
	{
		return this.parameters;
	}

	/**
	 * Set parameters, this overwrites all current parameters.
	 * 
	 * @param parameters parameters for this request
	 */
	public void setParameters(Map<String, String> parameters)
	{
		this.parameters = parameters;
	}
	
	/**
	 * Get the HTTP request method
	 * 
	 * @return HTTP request method
	 */
	public HTTPMethod getHttpMethod() {
		return httpMethod;
	}
}