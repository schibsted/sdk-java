package no.spp.sdk.oauth;

public class ClientCredentials {

    public String clientId;
    public String clientSecret;
    public String redirectUri;

    public ClientCredentials(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }

    public ClientCredentials(){}

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Override
    public String toString() {
        return "ClientCredentials{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='***'" +
                ", redirectUri='" + redirectUri + '\'' +
                '}';
    }
}
