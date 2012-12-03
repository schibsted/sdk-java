package no.spp.examples.api.models;

import net.sf.json.JSON;
import no.spp.sdk.net.HTTPMethod;

import java.io.Serializable;
import java.util.Map;

public class RequestResponse implements Serializable {
    private String endpoint;
    private HTTPMethod method;
    private String code;
    private JSON result;

    public RequestResponse(String endpoint, Map<String, String> parameters, HTTPMethod method, JSON result) {
        this.endpoint = endpoint;
        this.method = method;
        this.result = result;

        this.code = generateCode(endpoint, parameters, method);
    }

    public RequestResponse(String endpoint, HTTPMethod method, JSON result) {
        this(endpoint, null, method, result);
    }

    public RequestResponse(String endpoint, JSON result) {
        this(endpoint, HTTPMethod.GET, result);
    }

    private String generateCode(String endpoint, Map<String, String> parameters, HTTPMethod method) {
        StringBuilder builder = new StringBuilder();
        builder.append("SPPClient client = new SPPClient(...);\n");
        if (method == HTTPMethod.GET) {
            if (parameters == null || parameters.size() == 0) {
                builder.append("SPPClientResult result = client.GET(\"" + endpoint + "\");\n");
            } else {
                builder.append("SPPClientResult result = client.GET(\"" + endpoint);
                Boolean firstParameter = true;
                for (String key : parameters.keySet()) {
                    if (firstParameter) {
                        firstParameter = false;
                        builder.append("?" + key + "=" + parameters.get(key));
                    } else {
                        builder.append("&" + key + "=" + parameters.get(key));
                    }
                }
                builder.append("\");\n");
            }
        } else if (method == HTTPMethod.POST) {
            builder.append("Map<String, String> parameters = new HashMap<String, String>();\n");
            for (String key : parameters.keySet()) {
                builder.append("parameters.put(\"" + key + "\", \"" + parameters.get(key) +"\");\n");
            }
            builder.append("SPPClientResult result = client.POST(\"" + endpoint + "\", parameters);\n");
        }
        builder.append("System.out.println(response.getJSON().toString(3));\n");
        return builder.toString();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public JSON getResult() {
        return result;
    }

    public void setResult(JSON result) {
        this.result = result;
    }

    public String getFormatedResult() {
        return result.toString(3);
    }
}
