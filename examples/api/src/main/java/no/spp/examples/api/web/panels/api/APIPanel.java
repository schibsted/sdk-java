package no.spp.examples.api.web.panels.api;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import no.spp.examples.api.SPiDWebSession;
import no.spp.examples.api.models.RequestResponse;
import no.spp.examples.api.web.panels.code.CodePanel;
import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.SPPClientResponse;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.net.HTTPMethod;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIPanel extends Panel {

    public APIPanel(String id) {
        super(id);

        SPPClient client = SPiDWebSession.get().getSppClient();
        if (client != null) {
            ListView<RequestResponse> indexList = new ListView<RequestResponse>("indexList") {
                @Override
                protected void populateItem(ListItem<RequestResponse> item) {
                    RequestResponse requestResponse = item.getModelObject();
                    item.add(new ExternalLink("indexItem", "#api" + item.getIndex(), requestResponse.getEndpoint()));
                }
            };

            ListView<RequestResponse> apiList = new ListView<RequestResponse>("apiList") {
                @Override
                protected void populateItem(ListItem<RequestResponse> item) {
                    RequestResponse requestResponse = item.getModelObject();
                    item.setMarkupId("api" + item.getIndex() );
                    item.add(new CodePanel("code", requestResponse));
                }
            };

            List<RequestResponse> requestResponseList = new ArrayList<RequestResponse>();
            try {
                requestResponseList.add(getMeRequest(client));
                requestResponseList.add(getUserRequest(client));
                requestResponseList.add(getUserOrdersRequest(client));
                requestResponseList.add(getUsersRequest(client));
                requestResponseList.add(postProductRequest(client));
                requestResponseList.add(getProductsRequest(client));
                requestResponseList.add(getReportTemplateRequest(client));
                requestResponseList.add(getDiscountsRequest(client));

            } catch (SPPClientRefreshTokenException e) {
                System.out.println("SPPClientRefreshTokenException, error message: " + e.getMessage());
                throw new RestartResponseAtInterceptPageException(InternalErrorPage.class);
            } catch (SPPClientException e) {
                System.out.println("SPPClientException, error message: " + e.getMessage());
                throw new RestartResponseAtInterceptPageException(InternalErrorPage.class);
            }

            indexList.setList(requestResponseList);
            apiList.setList(requestResponseList);

            add(indexList);
            add(apiList);
        }
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(SPiDWebSession.get().getSppClient() != null);
    }

    private RequestResponse getMeRequest(SPPClient client) throws SPPClientRefreshTokenException, SPPClientException {
        String endpoint = "/me";

        JSON json;
        try {
            SPPClientResponse response = client.GET(endpoint);
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }
        return new RequestResponse(endpoint, json);
    }

    private RequestResponse getUserRequest(SPPClient client) throws SPPClientRefreshTokenException, SPPClientException {
        String endpoint = "/user/" + client.getOauthCredentials().getUserId();

        JSON json;
        try {
            SPPClientResponse response = client.GET(endpoint);
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }
        return new RequestResponse(endpoint, json);
    }

    private RequestResponse getUsersRequest(SPPClient client) throws SPPClientRefreshTokenException, SPPClientException {
        String endpoint = "/users";

        JSON json;
        try {
            SPPClientResponse response = client.GET(endpoint);
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }
        return new RequestResponse(endpoint, json);
    }

    private RequestResponse getUserOrdersRequest(SPPClient client) throws SPPClientRefreshTokenException, SPPClientException {
        String endpoint = "/user/" + client.getOauthCredentials().getUserId() + "/orders";

        JSON json;
        try {
            SPPClientResponse response = client.GET(endpoint);
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }
        return new RequestResponse(endpoint, json);
    }

    private RequestResponse postProductRequest(SPPClient client) throws SPPClientRefreshTokenException, SPPClientException {
        String endpoint = "/product";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("code", "test2");
        parameters.put("name", "name");
        parameters.put("price", "9900");
        parameters.put("vat", "2500");
        parameters.put("paymentOptions", "2");
        parameters.put("type", "1");
        parameters.put("currency", "NOK");

        JSON json;
        try {
            SPPClientResponse response = client.POST(endpoint, parameters);
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }

        return new RequestResponse(endpoint, parameters, HTTPMethod.POST, json);
    }

    private RequestResponse getProductsRequest(SPPClient client) throws SPPClientRefreshTokenException, SPPClientException {
        String endpoint = "/products";

        JSON json;
        try {
            SPPClientResponse response = client.GET(endpoint, new HashMap<String, String>());
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }
        return new RequestResponse(endpoint, json);
    }

    private RequestResponse getReportTemplateRequest(SPPClient client) throws SPPClientException, SPPClientRefreshTokenException {
        String endpoint = "/reports/template/1";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("from", "1970-01-01");
        parameters.put("to", "2100-01-01");

        JSON json;
        try {
            SPPClientResponse response = client.GET(endpoint, parameters);
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }
        return new RequestResponse(endpoint, parameters, HTTPMethod.GET, json);
    }

    private RequestResponse getDiscountsRequest(SPPClient client) throws SPPClientRefreshTokenException, SPPClientException {
        String endpoint = "/discounts";

        JSON json;
        try {
            SPPClientResponse response = client.GET(endpoint);
            json = response.getJSON();
        } catch (SPPClientResponseException e) {
            json = JSONSerializer.toJSON(e.getResponseBody());
        }
        return new RequestResponse(endpoint, json);
    }
}