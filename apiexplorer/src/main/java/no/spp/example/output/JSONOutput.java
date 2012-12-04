package no.spp.example.output;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONOutput implements Output {
    @SuppressWarnings("rawtypes")
    private Map data = new HashMap();


    public void process(String action, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().println(JSONObject.fromObject(data));
    }

    public void setUseLayout(Boolean useLayout) {
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setOutputData(Map data) {
        this.data.putAll(data);
    }

}
