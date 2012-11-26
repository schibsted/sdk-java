package no.spp.examples.paylink.web.pages.homepage;

import no.spp.examples.paylink.ApplicationSettings;
import no.spp.examples.paylink.models.Paylink;
import no.spp.examples.paylink.web.pages.paylink.PaylinkPage;
import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.SPPClientResponse;
import no.spp.sdk.client.ServerClientBuilder;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.oauth.ClientCredentials;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.pages.InternalErrorPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.RangeValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        add(new FeedbackPanel("feedback").setOutputMarkupId(true));
        add(new CreatePaylinkForm("form"));
    }

    private class CreatePaylinkForm extends Form<Paylink> {
        /**
         * Construct.
         *
         * @param name Component name
         */
        @SuppressWarnings("serial")
        public CreatePaylinkForm(String name) {
            super(name, new CompoundPropertyModel<Paylink>(new Paylink()));

            add(new TextField<String>("title").setRequired(true));

            List<String> list = new ArrayList<String>();
            list.add("AUTHORIZE");
            list.add("DIRECT");
            add(new DropDownChoice<String>("flow", list).setRequired(true));

            add(new TextField<String>("description").setRequired(true));

            add(new TextField<Integer>("price", Integer.class).setRequired(true).add(
                    new RangeValidator<Integer>(0, Integer.MAX_VALUE)));

            add(new Button("saveButton"));
        }

        @Override
        public void onSubmit() {
            ClientCredentials clientCredentials = new ClientCredentials(ApplicationSettings.CLIENT_ID,
                    ApplicationSettings.CLIENT_SECRET,
                    ApplicationSettings.REDIRECT_URI);
            try {
                SPPClient client = new ServerClientBuilder(clientCredentials)
                        .withApiVersion(ApplicationSettings.API_VERSION)
                        .withBaseUrl(ApplicationSettings.SERVER_URL)
                        .build();
                Map<String, String> data = new HashMap<String, String>();

                Paylink paylink = getModelObject();
                data.put("purchaseFlow", paylink.getFlow());
                data.put("title", paylink.getTitle());
                data.put("items", "[{\"vat\": 2500, \"price\":" + (paylink.getPrice() * 100) + ", \"description\":\"" + paylink.getDescription() + "\"}]");

                SPPClientResponse response = client.POST("/paylink", data);
                setResponsePage(new PaylinkPage(getPageParameters(), response));
            } catch (SPPClientException e) {
                System.out.println(e.getMessage());
                throw new RestartResponseAtInterceptPageException(InternalErrorPage.class);
            } catch (SPPClientResponseException e) {
                System.out.println(e.getMessage());
                throw new RestartResponseAtInterceptPageException(InternalErrorPage.class);
            } catch (SPPClientRefreshTokenException e) {
                System.out.println(e.getMessage());
                throw new RestartResponseAtInterceptPageException(InternalErrorPage.class);
            }
        }
    }
}
