package no.spp.sdk.client;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import no.spp.sdk.exception.SPPClientResponseException;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals("value for key foo should be bar", ((JSONObject) json).getString("foo"), "bar");

        response = new SPPClientResponse(200, "{\"data\":[\"foo\"]}", "2", 1);
        json = response.getJSON();
        assertTrue("json should be an array", json.isArray());
        assertEquals("value for index 0 should be foo", ((JSONArray) json).get(0), "foo");
    }
}

