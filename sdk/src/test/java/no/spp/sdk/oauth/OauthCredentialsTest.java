package no.spp.sdk.oauth;

import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;
import net.smartam.leeloo.client.response.OAuthClientResponseFactory;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OauthCredentialsTest {

    private OAuthAccessTokenResponse resp;
    private OauthCredentials oauthCredentials;

    private OAuthAccessTokenResponse getOAuthAccessTokenResponseWithExpires(int expires) throws OAuthSystemException, OAuthProblemException {
        String s = "{\"access_token\":\"211b54aaf8e9c36311b9c3fb5773e4a2e314c2c1\",\"expires_in\":" + expires + ",\"scope\":null,\"user_id\":false,\"refresh_token\":\"d7fbd1482eff47954feac77d70994036ee597a09\",\"server_time\":1320127907}";
        return OAuthClientResponseFactory.createCustomResponse(s, "application/json", 200, OAuthJSONAccessTokenResponse.class);
    }

    @Before
    public void setUp() throws OAuthProblemException, OAuthSystemException {
        oauthCredentials = new OauthCredentials(getOAuthAccessTokenResponseWithExpires(3600));
    }

    @Test
    public void returnsCorrectAccessToken() throws Exception {
        assertEquals(oauthCredentials.getAccessToken(), "211b54aaf8e9c36311b9c3fb5773e4a2e314c2c1");
    }

    @Test
    public void returnsCorrectRefreshToken() throws Exception {
        assertEquals("Should be equal", "d7fbd1482eff47954feac77d70994036ee597a09", oauthCredentials.getRefreshToken());
    }

    @Test
    public void expiresInReturnsCorrectly() throws Exception {
        assertEquals("Should be equal", oauthCredentials.getExpiresInSeconds(), "3600");
    }

    @Test
    public void whenExpiresIn3600CredentialsIsNotExpiredNow() throws Exception {
        assertFalse(oauthCredentials.isExpiredNow());
    }

    @Test
    public void whenExpiresIn0CredentialsIsNotExpiredNow() throws Exception {
        oauthCredentials = new OauthCredentials(getOAuthAccessTokenResponseWithExpires(0));
        assertTrue("Should be expired", oauthCredentials.isExpiredNow());
    }

}
