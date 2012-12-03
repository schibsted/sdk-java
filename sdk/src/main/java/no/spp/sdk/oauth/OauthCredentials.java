package no.spp.sdk.oauth;

import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

public class OauthCredentials implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(OauthCredentials.class);

    private final Date expiresDate;
    private final String userId;
    private final String accessToken;
    private final String refreshToken;
    private final String expiresInSeconds;

    public OauthCredentials(String accessToken, String refreshToken, String expiresInSeconds, String userId){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInSeconds = expiresInSeconds;
        this.expiresDate = getExpiryDateFromExpiresIn(expiresInSeconds);
        this.userId = userId;
    }

    public OauthCredentials(OAuthAccessTokenResponse oAuthAccessTokenResponse){
        this.accessToken = oAuthAccessTokenResponse.getAccessToken();
        this.refreshToken = oAuthAccessTokenResponse.getRefreshToken();
        this.expiresInSeconds = oAuthAccessTokenResponse.getExpiresIn();
        this.expiresDate = getExpiryDateFromExpiresIn(expiresInSeconds);
        this.userId = oAuthAccessTokenResponse.getParam("user_id");
    }

    private Date getExpiryDateFromExpiresIn(String expiresInSeconds) {
        long tenSecondsBeforeExpiryMillis = (Integer.parseInt(expiresInSeconds) - 10) * 1000L;
        return new Date(System.currentTimeMillis() + tenSecondsBeforeExpiryMillis);
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getExpiresInSeconds() {
        return this.expiresInSeconds;
    }

    public boolean isExpiredNow(){
        return isExpired(new Date());
    }

    public boolean isExpired(Date when){
        return expiresDate.before(when);
    }

    public String getUserId() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "OauthCredentials{" +
                "expires=" + expiresDate +
                ", userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresInSeconds='" + expiresInSeconds + '\'' +
                '}';
    }
}
