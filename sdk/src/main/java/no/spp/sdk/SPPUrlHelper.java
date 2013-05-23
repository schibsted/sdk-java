package no.spp.sdk;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/** Used to help build URLs
 *
 */
public class SPPUrlHelper {

    private String sppBaseUrl;
    private String clientId;

    public SPPUrlHelper(String clientId, String sppBaseUrl) {
        this.clientId = clientId;
        this.sppBaseUrl = sppBaseUrl;
    }

    /**
        * Get a Purchase URI suitable for use with redirects.<br>
        * <p/>
        * The parameters:<br>
        * - redirect_uri: (required)<br>
        * - product_id: preselect a specific product (skip choose product step)
        *
        * @param inputParams custom parameters for the uri
        * @return URI to product purchase
        */
       public String getPurchaseURI(String accessToken, Map<String, String> inputParams) {
           Map<String, String> params = new HashMap<String, String>(inputParams);
           params.put("flow", "payment");
           params.put("client_id", clientId);
           params.put("response_type", "code");
           params.put("oauth_token", accessToken);

           return getUrl(SPPURLName.WWW, "auth/start", params);
       }

       /**
        * Get a Logout URI suitable for use with redirects.<br>
        * <p/>
        * The parameters:<br>
        * - redirect_uri: (required) the URI to go to after a successful logout<br>
        *
        * @param inputParams custom parameters for the URI
        * @return the URI for the logout flow
        */
       public String getLogoutURI(String accessToken, Map<String, String> inputParams) {
           Map<String, String> params = new HashMap<String, String>(inputParams);
           params.put("oauth_token", accessToken);

           return getUrl(SPPURLName.WWW, "logout", params);
       }

       /**
        * Get the URI for redirecting the user to his VG account page
        *
        * @param inputParams custom parameters for the URI
        * @return the URI for the account page
        */
       public String getAccountURI(Map<String, String> inputParams) {
           Map<String, String> params = new HashMap<String, String>(inputParams);
           params.put("client_id", clientId);

           return getUrl(SPPURLName.WWW, "account", params);
       }

       /**
         * Get the URI for redirecting the user to his VG subscriptions page
         *
         * @param inputParams custom parameters for the URI
         * @return the URI for the subscriptions page
         */
        public String getSubscriptionsURI(Map<String, String> inputParams) {
            Map<String, String> params = new HashMap<String, String>(inputParams);
            params.put("client_id", clientId);

            return getUrl(SPPURLName.WWW, "account/subscriptions", params);
        }

       /**
        * Get the URI for redirecting the user to his VG purchase history page
        *
        * @param inputParams custom parameters for the URI
        * @return the URI for the purchase history page
        */
       public String getPurchaseHistoryURI(Map<String, String> inputParams) {
           Map<String, String> params = new HashMap<String, String>(inputParams);
           params.put("client_id", clientId);

           return getUrl(SPPURLName.WWW, "account/purchasehistory", params);
       }

       /**
        * Get a login status URI to fetch the status from SPPServices.<br>
        * <p/>
        * The parameters:<br>
        * - ok_session: the URI to go to if a session is found<br>
        * - no_session: the URI to go to if the user is not connected<br>
        * - no_user: the URI to go to if the user is not signed into SPPServices<br>
        *
        *
        * @param inputParams custom parameters for the URI
        * @return the URI for Login status
        */
       public String getLoginStatusURI(Map<String, String> inputParams) {
           Map<String, String> params = new HashMap<String, String>(inputParams);
           params.put("client_id", clientId);
           params.put("session_version", "1");
           params.put("response_type", "code");

           return getUrl(SPPURLName.WWW, "login_status", params);
       }

       /**
        * Get a Signup URI for use with redirects. By default, full page redirect is
        * assumed. If you are using the generated URI with a window.open() call in
        * JavaScript, you can pass in display=popup as part of the params.<br>
        * <p/>
        * The parameters:<br>
        * - redirect_uri: (required) the URI to go to after a successful login<br>
        * - cancel_url: the URI to go to after the user cancels<br>
        * - display: can be "page" (default, full page) or "popup"<br>
        *
        * @param inputParams custom parameters for the URI
        * @return the URI for the login flow
        */
       public String getSignupURI( Map<String, String> inputParams) {
           Map<String, String> params = new HashMap<String, String>(inputParams);
           params.put("client_id", clientId);
           params.put("response_type", "code");
           params.put("flow", "signup");
           params.put("return_session", "1");
           params.put("session_version", "1");

           return getUrl(SPPURLName.WWW, "signup", params);
       }

       /**
        * Get a Login URI for use with redirects. By default, full page redirect is
        * assumed. If you are using the generated URI with a window.open() call in
        * JavaScript, you can pass in display=popup as part of the params.<br>
        * <p/>
        * The parameters:<br>
        * - redirect_uri: (required) the URI to go to after a successful login<br>
        * - cancel_url: the URI to go to after the user cancels<br>
        * - display: can be "page" (default, full page) or "popup"<br>
        *
        * @param inputParams custom parameters for the URI
        * @return the URI for login
        */
       public String getLoginURI(Map<String, String> inputParams) {
           Map<String, String> params = new HashMap<String, String>(inputParams);
           params.put("client_id", clientId);
           params.put("response_type", "code");
           params.put("flow", "signup");
           params.put("return_session", "1");
           params.put("session_version", "1");

           return getUrl(SPPURLName.WWW, "login", params);
       }


    /**
     * Get the URL to a given SPPURLName
     *
     * @param name
     * @param apiVersion this is a version string that will be appended for API and API_READ URL's.
      *
     * @return URL to given SPPURLName
     */
    public String getBaseURL(SPPURLName name, String apiVersion) {
        switch (name) {
            case API:
            case API_READ:
                return sppBaseUrl + "/" + name.toString() + "/" + (apiVersion == null ? "" : apiVersion + "/");
            case WWW:
            default:
                return sppBaseUrl + "/";
        }
    }

    /**
     * Build the URI for given domain alias, path and parameters.
     *
     * @param name
     * @param path
     * @param params
     * @return the URI for the given parameters
     */
    private String getUrl(SPPURLName name, String path, Map<String, String> params) {
        String url = getBaseURL(name, null);

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        return url + path + "?" + encodeParameters(params);
    }

    /**
     * URLEncode parameter map.
     *
     * @param params
     * @return URLEncoded parameters.
     */
    private String encodeParameters(Map<String, String> params) {
        List<String> encodedParams = new ArrayList<String>();
        try {
            for (String key : params.keySet()) {
                encodedParams.add(key + "=" + URLEncoder.encode(params.get(key), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return StringUtils.join(encodedParams, "&");
    }

}
