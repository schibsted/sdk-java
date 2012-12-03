package no.spp.sdk;


import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.UserClientBuilder;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.fake.FakeHTTPClientWithFixedResponse;
import no.spp.sdk.net.HTTPClient;
import no.spp.sdk.oauth.ClientCredentials;
import no.spp.sdk.oauth.OauthCredentials;
import no.spp.sdk.oauth.OauthHelper;
import org.junit.Before;

import java.util.Calendar;

public class SPPClientBuilderTest {

    public static final String REDIRECT_URI = "redirect://uri";
    public static final String SECRET = "secret";
    public static final String ID = "id";
    public static final String SAMPLE_ACCESS_TOKEN = "590e6587a4cbe1fafbce1a442031a1db9ace1fea";
    public static final String SAMPLE_REFRESH_TOKEN = "7a33e5974155bb9a4a46e61b7f7f052672889d62";
    public static final int EXPIRES = 3600;
    public static final String SPP_BASE_URL = "http://base.url";

    private static String getAccessTokenResponseJson(){
        return getAccessTokenResponseJson(SAMPLE_ACCESS_TOKEN, EXPIRES, SAMPLE_REFRESH_TOKEN);
    }
    private static String getAccessTokenResponseJson(String nakedToken, int expires, String refreshToken){
        return "{\"access_token\":\"" + nakedToken + "\",\"expires_in\":" + expires + ",\"scope\":null,\"user_id\":false,\"refresh_token\":\"" + refreshToken + "\",\"server_time\":1319803481}";

    }
    @Before
    public void createSPPUserClient() throws SPPClientException, OAuthProblemException, OAuthSystemException {
        ClientCredentials credentials = new ClientCredentials(ID, SECRET, REDIRECT_URI);
        OauthHelper oauthHelper = new OauthHelper(
                SPP_BASE_URL,
            credentials,
            new FakeHTTPClientWithFixedResponse(getAccessTokenResponseJson()));
        OauthCredentials tokenResponse =  oauthHelper.getServerAccessToken();

        SPPClient sppClient = new UserClientBuilder(credentials)
                .withUserOauthCredentials(tokenResponse)
                .withBaseUrl(SPP_BASE_URL)
                .build();
    }


    private String clientId;
    private String clientSecret;
    private String redirectURI;
    private OAuthAccessTokenResponse oauthTokenRespone;
    private Boolean production = false;
    private Boolean isServerToServer = false;
    private Boolean autoRefreshToken = true;
    private Calendar tokenExpires;
    private String apiVersion = null;
    private HTTPClient httpClient;
    private String sppBaseUrl;
}

