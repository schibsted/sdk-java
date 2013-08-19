package no.spp.sdk.oauth;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;

import java.io.Serializable;
import java.util.Date;

public class OauthCredentials implements Serializable {

    private static final long serialVersionUID = -1860557532714812684L;

    private final Date expiresDate;
    private final String accessToken;
    private final String refreshToken;
    private final String expiresInSeconds;
    private final String userId;

    public OauthCredentials(String accessToken, String refreshToken, String expiresInSeconds){
        this(accessToken, refreshToken, expiresInSeconds, "");
    }

    public OauthCredentials(String accessToken, String refreshToken, String expiresInSeconds, String userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresInSeconds = expiresInSeconds;
        this.userId = userId;
        this.expiresDate = getExpiryDateFromExpiresIn(expiresInSeconds);
    }

    public OauthCredentials(OAuthAccessTokenResponse oAuthAccessTokenResponse){
        accessToken = oAuthAccessTokenResponse.getAccessToken();
        refreshToken = oAuthAccessTokenResponse.getRefreshToken();
        expiresInSeconds = oAuthAccessTokenResponse.getExpiresIn();
        expiresDate = getExpiryDateFromExpiresIn(expiresInSeconds);
        String body = oAuthAccessTokenResponse.getBody();
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(body);
        userId = jsonObject.get("user_id").toString();
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
        return userId;
    }

    @Override
    public String toString() {
        return "OauthCredentials{" +
                "expires=" + expiresDate +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresInSeconds='" + expiresInSeconds + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }


}
