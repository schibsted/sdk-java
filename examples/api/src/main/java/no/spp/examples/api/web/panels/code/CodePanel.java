package no.spp.examples.api.web.panels.code;

import no.spp.examples.api.SPiDWebSession;
import no.spp.examples.api.models.RequestResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class CodePanel extends Panel {

    public CodePanel(String id, RequestResponse requestResponse) {
        super(id);

        add(new Label("request", requestResponse.getMethod() + " " + requestResponse.getEndpoint()));
        add(new Label("code", requestResponse.getCode()));
        add(new Label("result", requestResponse.getFormatedResult()));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(SPiDWebSession.get().getSppClient() != null);
    }
}
