package no.spp.examples.clientlogin;

import no.spp.sdk.client.SPPClient;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class SPiDWebSession extends WebSession {
    private SPPClient sppClient;

    public SPiDWebSession(Request request) {
        super(request);
    }

    public final SPPClient getSppClient() {
        return sppClient;
    }

    public final void setSppClient(SPPClient sppClient) {
        this.sppClient = sppClient;
    }

    public static SPiDWebSession get() {
        return (SPiDWebSession) Session.get();
    }
}
