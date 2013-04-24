package no.spp.sdk.client;

import no.spp.sdk.exception.HTTPClientException;
import no.spp.sdk.net.HTTPClientResponse;
import no.spp.sdk.net.URLConnectionClient;
import org.junit.Before;
import org.junit.Test;
import no.spp.sdk.client.SPPClientResponse;
import no.spp.sdk.exception.SPPClientResponseException;

import net.sf.json.JSON;   
import net.sf.json.JSONException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SPPClientResponseTest {
    @Before
    public void setUp() {
    }

    @Test
    public void getJSONObjectJSONException() throws SPPClientResponseException {
        SPPClientResponse response = new SPPClientResponse(200, "{\"data\":[]}", "2", 1);
        try {
            response.getJSONObject();
            fail("getJSONObject() should've thrown an exception version 2!");
        } catch (JSONException e) {
            
        }

        //test version 1
        response = new SPPClientResponse(200, "[]", "1", 1);
        try {
            response.getJSONObject();
            fail("getJSONObject() should've thrown an exception on version 1 api!");
        } catch (JSONException e) {
            
        }
        
    }

    @Test
    public void getJSONArrayJSONException() throws SPPClientResponseException {
        SPPClientResponse response = new SPPClientResponse(200, "{\"data\":{}}", "2", 1);
        try {
            response.getJSONArray();
            fail("getJSONArray() should've thrown an exception version 2!");
        } catch (JSONException e) {
            
        }

        //test version 1
        response = new SPPClientResponse(200, "{}", "1", 1);
        try {
            response.getJSONArray();
            fail("getJSONArray() should've thrown an exception on version 1 api!");
        } catch (JSONException e) {
            
        }
        
    }

    @Test
    public void getJSON() throws SPPClientResponseException {
        SPPClientResponse response = new SPPClientResponse(200, "{\"data\":{\"foo\":\"bar\"}}", "2", 1);
        JSON json = response.getJSON();
        assertFalse("json should not be an array", json.isArray());        
        assertEquals("value for key foo should be bar", ((JSONObject)json).getString("foo"), "bar");

        response = new SPPClientResponse(200, "{\"data\":[\"foo\"]}", "2", 1);
        json = response.getJSON();
        assertTrue("json should be an array", json.isArray());        
        assertEquals("value for index 0 should be foo", ((JSONArray)json).get(0), "foo");
    }

    //mail=erikvikt3%40dispostable.com&oauth_token=6e5979f75a19ba86bda73e3f3cf4a34f1ad022ea&redirectUri=http%3A%2F%2Flocalhost.aftonbladet.se%2FafterSpp%3Fservice%3Dplus%26returnPath%3Dhttp%3A%2F%2Fstage.mobil.aftonbladet.se%2Fwywallet" "https://stage.payment.schibsted.se/api/2/user"

//    @Test
//    public void getFollowingCreateShouldGetReasonableResponseCode() throws HTTPClientException {
//        //Change to something valid
//        String oauth_token = "6e5979f75a19ba86bda73e3f3cf4a34f1ad022ea";
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("email", "erikvikt3@dispostable.com");
//        params.put("oauth_token", oauth_token);
//        try {
//
//            HTTPClientResponse createResponse = new URLConnectionClient().execute("https://stage.payment.schibsted.se/api/2/user", params, null, "POST");
//            assertTrue(createResponse.getResponseCode() == 302);
//        } catch (HTTPClientException e) {
//
//        }
//        HTTPClientResponse getResponse = new URLConnectionClient().execute("https://stage.payment.schibsted.se/api/2/users", params, null, "GET");
//        assertFalse(getResponse.getResponseCode() == -1);
//    }
}

