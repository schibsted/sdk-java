package no.spp.examples.clientlogin;

import no.spp.sdk.client.SPPClient;
import org.apache.wicket.MetaDataKey;

public class ApplicationSettings {
    public static final String CLIENT_ID = "YOUR-CLIENT-ID";
    public static final String CLIENT_SECRET = "YOUR-CLIENT-SECRET";
    public static final String REDIRECT_URI = "YOUR-REDIRECT-URI";
    public static final String SERVER_URL = "YOUR-SPID-SERVER-URL";
    public static final String API_VERSION = "2";

    public static final MetaDataKey<SPPClient> SPP_CLIENT_META_DATA_KEY = new MetaDataKey<SPPClient>()
    {
        private static final long serialVersionUID = 1L;
    };
}
