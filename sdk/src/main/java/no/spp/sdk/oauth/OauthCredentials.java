package no.spp.sdk.oauth;

import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;

import java.io.Serializable;
import java.util.Date;

public class OauthCredentials implements Serializable {

    private final Date expiresDate;
    private final String accessToken;
    private final String refreshToken;
    private final String expiresInSeconds;

    public OauthCredentials(String accessToken, String refreshToken, String expiresInSeconds){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInSeconds = expiresInSeconds;
        expiresDate = getExpiryDateFromExpiresIn(expiresInSeconds);
    }

    public OauthCredentials(OAuthAccessTokenResponse oAuthAccessTokenResponse){
        accessToken = oAuthAccessTokenResponse.getAccessToken();
        refreshToken = oAuthAccessTokenResponse.getRefreshToken();
        expiresInSeconds = oAuthAccessTokenResponse.getExpiresIn();
        expiresDate = getExpiryDateFromExpiresIn(expiresInSeconds);
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

    @Override
    public String toString() {
        return "OauthCredentials{" +
                "expires=" + expiresDate +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresInSeconds='" + expiresInSeconds + '\'' +
                '}';
    }
}
