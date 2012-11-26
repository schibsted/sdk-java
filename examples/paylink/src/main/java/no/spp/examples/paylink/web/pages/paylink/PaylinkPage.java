package no.spp.examples.paylink.web.pages.paylink;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import no.spp.examples.paylink.web.pages.homepage.HomePage;
import no.spp.sdk.client.SPPClientResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.LinkedList;
import java.util.List;

//import org.apache.wicket.ajax.json.JSONArray;
//import org.apache.wicket.ajax.json.JSONException;
//import org.apache.wicket.ajax.json.JSONObject;

public class PaylinkPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public PaylinkPage() {
        setResponsePage(new HomePage(new PageParameters()));
    }

    public PaylinkPage(final PageParameters parameters, SPPClientResponse response) {
        super(parameters);

        final String title = response.getJSONObject().get("title").toString();
        add(new Label("title", title));
        add(new Label("expires", response.getJSONObject().get("expires").toString()));
        String url = response.getJSONObject().get("shortUrl").toString();
        add(new ExternalLink("url", url, url));

        JSONArray items = response.getJSONObject().getJSONArray("items");
        List<JSONObject> itemList = new LinkedList<JSONObject>();
        for (Object object : items) {
            JSONObject jsonObject = (JSONObject) object;
            itemList.add(jsonObject);
        }

        ListView<JSONObject> listView = new ListView<JSONObject>("items", itemList) {
            @Override
            protected void populateItem(ListItem<JSONObject> item) {
                JSONObject object = item.getModelObject();
                String label = title + " : " + object.getString("currency") + " " + (object.getInt("price") / 100) + ",- (" + (object.getInt("vat") / 100 + "% VAT)");
                item.add(new Label("label", label));
            }
        };
        add(listView);
    }
}
