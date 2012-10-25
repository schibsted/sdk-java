package no.spp.example;

import net.sf.json.JSONObject;
import no.spp.example.controller.ServletController;
import no.spp.example.exception.ServletControllerException;
import no.spp.sdk.SPPUrlHelper;
import no.spp.sdk.client.*;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.net.HTTPMethod;
import no.spp.sdk.oauth.ClientCredentials;
import no.spp.sdk.oauth.OauthHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class APIExplorerServlet extends ServletController 
{
	private final Logger log = LoggerFactory.getLogger(APIExplorerServlet.class);
	private static final String SPP_CLIENT = "vgservices.client";
	private static final String SPP_AUTHENTICATED_USER = "vgservices.user";
	
	private Properties sppProps = new Properties();
	
	private List<String> validActions = new ArrayList<String>();
	
	public APIExplorerServlet()
	{
		validActions.add("index");
		validActions.add("authorize");
		validActions.add("authresponse");
		validActions.add("serverauth");
		validActions.add("logout");
		validActions.add("apirequest");
	}
	
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		
		try {
			sppProps.load(config.getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
		}
		catch (IOException e) {
		    log.error("No config file found. Please copy '/WEB-INF/config-dist.properties' to '/WEB-INF/config.properties' and fill out correct properties");
			e.printStackTrace();
		}		
	}
	
	public void indexAction() throws IOException, SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException
	{
		HttpSession session = request.getSession(true);
		SPPClient userClient = (SPPClient)session.getAttribute(SPP_CLIENT);
		
		Map<String, String> data = new HashMap<String, String>();
		
		if (userClient == null) {
			setOutput(null);
			response.sendRedirect("/explorer/authorize");
			return;
		}
		
		JSONObject authenticatedUser = (JSONObject)session.getAttribute(SPP_AUTHENTICATED_USER);
		if (authenticatedUser != null) {
			data.put("authenticatedAs", authenticatedUser.getString("displayName"));
		} else {
			data.put("authenticatedAs", "Server To Server");
		}

		String jsApiMethods = "";

        if(!userClient.getSppApi().getAPIVersion().equals("1") ){
		    SPPClientResponse apiEndpoints = userClient.GET("/endpoints");
            jsApiMethods = apiEndpoints.getJSONArray().toString();
            data.put("jsapimethods", jsApiMethods);
        } else {
            data.put("jsapimethods", "[]");
        }
		Map<String,String> params = new HashMap<String,String>();
		params.put("redirect_uri", request.getRequestURL().toString());

        SPPUrlHelper urls = new SPPUrlHelper(
            userClient.getClientCredentials().clientId,
            sppProps.getProperty("SPP_PROTOCOL_AND_DOMAIN") );

        String accessToken = userClient.getOauthCredentials().getAccessToken();

		data.put("getPurchaseURI", urls.getPurchaseURI(accessToken, params));
		data.put("getLogoutURI", urls.getLogoutURI(accessToken, params));
		data.put("getLoginURI", urls.getLoginURI(params));
		data.put("getSignupURI", urls.getSignupURI(params));
		
		params.clear();
		data.put("getAccountURI", urls.getAccountURI(params));
		data.put("getPurchaseHistoryURI", urls.getPurchaseHistoryURI(params));
		data.put("getLoginStatusURI", urls.getLoginStatusURI(params));

		setOutputData(data);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void authorizeAction() throws ServletControllerException
	{
		String url = "";
		
		try {
			url = OauthHelper.getAuthorizationURL(
                    sppProps.getProperty("CLIENT_ID"),
                    sppProps.getProperty("REDIRECT_DOMAIN") + "/explorer/authresponse",
                    sppProps.getProperty("SPP_PROTOCOL_AND_DOMAIN"));

		} catch (SPPClientException e) {
			throw new ServletControllerException(e);
		}

		Map data = new HashMap();
		data.put("userAuthURL", url);
		setOutputData(data);		
	}
	
	public void authresponseAction() throws ServletControllerException, IOException
	{
		SPPClient userClient;
		SPPClientResponse clientResponse;
		
		try {
            ClientCredentials clientCredentials = new ClientCredentials(sppProps.getProperty("CLIENT_ID"),
                                                                        sppProps.getProperty("CLIENT_SECRET"),
                                                                        sppProps.getProperty("REDIRECT_DOMAIN") + "/explorer");
            String authorizationCode = OauthHelper.getAuthorizationCode(request);

            userClient = new UserClientBuilder(clientCredentials)
                .withUserAuthorizationCode(authorizationCode)
                .withBaseUrl(sppProps.getProperty("SPP_PROTOCOL_AND_DOMAIN"))
                .withApiVersion(sppProps.getProperty("API_VERSION"))
                .build();


			clientResponse = userClient.GET("me");

		} catch (SPPClientException e) {
			throw new ServletControllerException(e);
		} catch (SPPClientResponseException e) {
			throw new ServletControllerException(e);
		} catch (SPPClientRefreshTokenException e) {
			throw new ServletControllerException(e);
		}
		
		HttpSession session = request.getSession(true);
		session.setAttribute(SPP_CLIENT, userClient);
		session.setAttribute(SPP_AUTHENTICATED_USER, clientResponse.getJSONObject());
		setOutput(null);
		response.sendRedirect("/explorer/index");
	}
	
	public void serverauthAction() throws ServletControllerException, IOException
	{
		SPPClient serverClient;
		try {
        ClientCredentials credentials = new ClientCredentials(
            sppProps.getProperty("CLIENT_ID"),
            sppProps.getProperty("CLIENT_SECRET"),
            sppProps.getProperty("REDIRECT_DOMAIN") + "/explorer");

        serverClient = new ServerClientBuilder(credentials)
            .withApiVersion(sppProps.getProperty("API_VERSION"))
            .withBaseUrl(sppProps.getProperty("SPP_PROTOCOL_AND_DOMAIN"))
            .build();
		} catch (SPPClientException e) {
			throw new ServletControllerException(e);
		}
		
		HttpSession session = request.getSession(true);
		session.setAttribute(SPP_CLIENT, serverClient);
		setOutput(null);
		response.sendRedirect("/explorer/index");
	}
	
	public void logoutAction() throws IOException
	{
		HttpSession session = request.getSession(true);
		session.removeAttribute(SPP_CLIENT);
		session.removeAttribute(SPP_AUTHENTICATED_USER);
		setOutput(null);
		response.sendRedirect("/explorer/index");
	}
	
	public void apirequestAction() throws ServletControllerException, IOException
	{
		HttpSession session = request.getSession(true);
		SPPClient client = (SPPClient)session.getAttribute(SPP_CLIENT);
		setOutput(null);
		response.setContentType("application/json");

        try {
            SPPClientRequest clientRequest =
                    new SPPClientRequest(getParameter("method"), extractParameters(), HTTPMethod.valueOf(getParameter("httpMethod")));
            SPPClientResponse clientResponse = client.makeApiRequest(clientRequest);
			response.getWriter().println(clientResponse.getData());
			
		} catch (SPPClientException e) {
			throw new ServletControllerException(e);
		} catch (SPPClientResponseException e) {
			response.getWriter().println(e.getResponseBody());			
		} catch (SPPClientRefreshTokenException e) {
			response.getWriter().println("{error: " + e.getMessage() + "}");
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Map<String, String> extractParameters()
	{
		List<String> excludeList = new ArrayList<String>();
		excludeList.add("httpMethod");
		excludeList.add("method");
        excludeList.add("page");
		
		Map params = getParameters();
		Map<String, String> result = new HashMap<String,String>();
		for (Object key : params.keySet()) {
			if (key instanceof String && params.get(key) instanceof String[] && !excludeList.contains(key)) {
				result.put((String)key, ((String[])params.get(key))[0]);
			}
		}
		
		return result;
	}
	
	@Override
	public List<String> getValidActions() 
	{		
		return validActions;
	}
}
