package no.spp.sdk.client;

import no.spp.sdk.exception.SPPClientAPISecurityException;
import no.spp.sdk.oauth.ClientCredentials;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * Class to handle security for client in API.
 *
 * @author Joakim Wånggren <joakim.wanggren@schibsted.se>
 */
public class SPPClientAPISecurity {

    ClientCredentials clientCredentials;

    public SPPClientAPISecurity(ClientCredentials clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public SPPClientResponse validateAndDecodeSignedResponse(SPPClientResponse response) throws SPPClientAPISecurityException {

        String b64signature = response.getResponseSignture();
        String algorithm = response.getResponseAlgorithm();
        String b64data = response.getData();
        byte[] digest;
        Mac mac;
        SecretKeySpec sks;

        if (algorithm == null) algorithm = "HMAC-SHA256";
        if (!algorithm.equals("HMAC-SHA256"))
            throw new SPPClientAPISecurityException("Hash algorithm not supported. Expected HMAC-SHA256");

        try {
            sks = new SecretKeySpec(this.clientCredentials.getClientSecret().getBytes("UTF-8"), "HmacSHA256");
            mac = Mac.getInstance("HmacSHA256");
            mac.init(sks);
            digest = mac.doFinal(b64data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException nsae) {
            throw new SPPClientAPISecurityException(nsae.getMessage());
        } catch (InvalidKeyException ike) {
            throw new SPPClientAPISecurityException(ike.getMessage());
        } catch (Exception e) {
            throw new SPPClientAPISecurityException(e.getMessage());
        }

        byte[] signature = base64UrlDecode(b64signature);
        if (!Arrays.equals(digest, signature))
            throw new SPPClientAPISecurityException("Digest does not match signature");

        byte[] data = base64UrlDecode(b64data);
        response.updateDataAfterDecryption(new String(data));

        return response;
    }

    public String generateHash(SPPClientRequest request) throws SPPClientAPISecurityException {
        Map<String, String> parameters = request.getParameters();
        return generateHash(parameters);
    }

    public String generateHash(Map<String, String> parameters) throws SPPClientAPISecurityException {
        String str = "";
        Mac mac;
        SecretKeySpec sks;

        String[] keys = parameters.keySet().toArray(new String[parameters.keySet().size()]);
        Arrays.sort(keys);

        for (String key : keys) {
            str += parameters.get(key);
        }

        try {
            sks = new SecretKeySpec(this.clientCredentials.getClientSecret().getBytes("UTF-8"), "HmacSHA256");
            mac = Mac.getInstance("HmacSHA256");
            mac.init(sks);
            byte[] digest = mac.doFinal(str.getBytes("UTF-8"));
            str = base64UrlEncode(digest);
        } catch (NoSuchAlgorithmException nsae) {
            throw new SPPClientAPISecurityException(nsae.getMessage());
        } catch (InvalidKeyException ike) {
            throw new SPPClientAPISecurityException(ike.getMessage());
        } catch (Exception e) {
            throw new SPPClientAPISecurityException(e.getMessage());
        }

        return str;
    }

    private byte[] base64UrlDecode(String str) {
        return Base64.decodeBase64(str.replace("-", "+").replace("_", "/").trim());
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.encodeBase64URLSafeString(bytes).replace("+", "-").replace("/", "_");
    }
}
