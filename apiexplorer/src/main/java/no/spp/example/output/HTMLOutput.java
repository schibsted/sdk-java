package no.spp.example.output;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HTMLOutput implements Output {
    @SuppressWarnings("rawtypes")
    private Map data = new HashMap();

    private Configuration configuration;
    private Boolean useLayout = true;

    public HTMLOutput(ServletContext context) {
        try {
            initializeFreeMarker(context.getResource("/templates"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void process(String action, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("text/html");
            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());

            Template template;
            if (useLayout) {
                data.put("contentTemplate", action + ".html");
                template = configuration.getTemplate("layout.html");
            } else {
                template = configuration.getTemplate(action + ".html");
            }
            template.process(data, writer);
            writer.flush();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setOutputData(Map data) {
        this.data.putAll(data);
    }

    private void initializeFreeMarker(URL templatePath) throws IOException {
        configuration = new Configuration();

        try {
            configuration
                    .setDirectoryForTemplateLoading(new File(templatePath.toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        configuration.setObjectWrapper(new DefaultObjectWrapper());
    }

    public void setUseLayout(Boolean useLayout) {
        this.useLayout = useLayout;
    }

}
