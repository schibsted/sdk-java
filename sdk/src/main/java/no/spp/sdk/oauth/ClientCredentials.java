package no.spp.sdk.oauth;

public class ClientCredentials {

    public String clientId;
    public String clientSecret;
    public String clientSignSecret;
    public String redirectUri;

    public ClientCredentials(String clientId, String clientSecret, String clientSignSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientSignSecret = clientSignSecret;
        this.redirectUri = redirectUri;
    }

    /**
     * @deprecated Use constructor with signature secret instead. ClientCredentials(String, String, String, String)
     */
    public ClientCredentials(String clientId, String clientSecret, String redirectUri) {
        this(clientId,clientSecret,"",redirectUri);
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

    public String getClientSignSecret() {
        return clientSignSecret;
    }

    public void setClientSignSecret(String clientSignSecret) {
        this.clientSignSecret = clientSignSecret;
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
