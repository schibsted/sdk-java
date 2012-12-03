package no.spp.sdk;

import no.spp.sdk.client.SPPClientRequest;
import no.spp.sdk.net.HTTPMethod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SPPClientRequestTest {

    public static final String PATH = "me";

    @Test
    public void pathGetsSet(){
        SPPClientRequest req = new SPPClientRequest(PATH, null, HTTPMethod.GET);
        assertEquals("Path incorrect", req.getEndpoint(), PATH);
    }

    @Test
    public void leadingSlashInPathGetsRemoved(){
        String path = "/me";
        SPPClientRequest req = new SPPClientRequest(path, null, HTTPMethod.GET);
        assertEquals("Path incorrect", req.getEndpoint(), PATH);
    }

    @Test
    public void slashInMiddleOfPathRemains(){
        String path = "user/id";
        SPPClientRequest req = new SPPClientRequest(path, null, HTTPMethod.GET);
        assertEquals("Path incorrect", req.getEndpoint(), path);
    }
}
