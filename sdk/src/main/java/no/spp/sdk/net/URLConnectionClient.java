package no.spp.sdk.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthClientResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import no.spp.sdk.exception.HTTPClientException;

/**
 * Basic HTTPClient implementation for making simple HTTP requests.
 * 
 * @author Martin Jonsson <martin.jonsson@gmail.com>
 *
 */
public class URLConnectionClient implements HTTPClient {
	private static final Logger log = LoggerFactory.getLogger(URLConnectionClient.class);
    private Integer connectTimeout = 5000; //in milliseconds  
    private Integer readTimeout = 30000; //in milliseconds
    private net.smartam.leeloo.client.URLConnectionClient oauthURLConnectionClient = new net.smartam.leeloo.client.URLConnectionClient();
    
	public HTTPClientResponse execute(String url, Map<String, String> parameters,
			Map<String, String> headers, String requestMethod)
			throws HTTPClientException {

        HttpURLConnection conn;
        URL urlObject;
        String body = null;
		try {
			if (parameters != null && !parameters.isEmpty()) {
				List<String> encodedParams = new ArrayList<String>();
				for (String key : parameters.keySet()) {
					encodedParams.add(key + "=" + URLEncoder.encode(parameters.get(key), "UTF-8"));
				}
				
				if (requestMethod.equalsIgnoreCase("POST")) {
					urlObject = new URL(url);
					body = StringUtils.join(encodedParams, "&"); 
				} else {
					urlObject = new URL(url + "?" + StringUtils.join(encodedParams, "&"));
				}

			} else {
				urlObject = new URL(url);
			}
						
			log.debug("Requesting url: {}", url);
			conn = (HttpURLConnection)urlObject.openConnection();
			conn.setReadTimeout(this.readTimeout);
			conn.setConnectTimeout(this.getConnectTimeout());
			conn.setRequestMethod(requestMethod);			
			conn.setInstanceFollowRedirects(true);
			
			if (headers != null) {				
				for (String key : headers.keySet()) {					
					conn.setRequestProperty(key, headers.get(key));
				}				
			}
            
			if (body != null) {
				log.debug("Request method {} body {}", requestMethod, body);
                conn.setDoOutput(true);
                OutputStream out = conn.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print(body);
                pw.flush();
                pw.close();
            }
			
            conn.connect();
            InputStream inputStream;
            Integer responseCode = conn.getResponseCode();
            
            log.debug("Response code {}", responseCode);
            
            if ((responseCode >= 200 && responseCode < 300) || responseCode == 302) {
            	inputStream = conn.getInputStream();
            } else {            	
            	inputStream = conn.getErrorStream();
            }

            Writer writer = getWriter(inputStream);

            log.debug("Response body: {}", writer.toString());

            return new HTTPClientResponse(responseCode, writer.toString());
		} catch (IOException e) {
			throw new HTTPClientException(e);			
		}
	}

    private Writer getWriter(InputStream inputStream) throws IOException {
        Writer writer = new StringWriter();

        if (inputStream != null) {
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
        }
        return writer;
    }

    /**
	 * Get the connect timeout setting
	 * 
	 * @return the current setting for connect timeout in ms.
	 */
	public Integer getConnectTimeout()
	{
		return this.connectTimeout;
	}

	/**
	 * Set connect timeout setting
	 * 
	 * @param connectTimeout in ms
	 */
	public void setConnectTimeout(Integer connectTimeout)
	{
		this.connectTimeout = connectTimeout;
	}

	/**
	 * Get read timeout setting
	 *  
	 * @return read timeout setting in ms
	 */
	public Integer getReadTimeout() 
	{
		return readTimeout;
	}

	/**
	 * Set read timeout setting
	 * 
	 * @param readTimeout in ms
	 */
	public void setReadTimeout(Integer readTimeout) 
	{
		this.readTimeout = readTimeout;
	}

	public <T extends OAuthClientResponse> T execute(
			OAuthClientRequest request, Map<String, String> headers,
			String requestMethod, Class<T> responseClass)
			throws OAuthSystemException, OAuthProblemException {
		return oauthURLConnectionClient.execute(request, headers, requestMethod, responseClass);
	}


}
