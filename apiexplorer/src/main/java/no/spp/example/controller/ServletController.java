package no.spp.example.controller;

import no.spp.example.output.HTMLOutput;
import no.spp.example.output.JSONOutput;
import no.spp.example.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class ServletController extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ServletController.class);

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    private Output output;

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.response = resp;
        this.request = req;

        String page = req.getParameter("page");
        if (page == null) {
            page = "";
        }

        setOutput(createOutput());

        String[] arguments = page.split("/");
        String actionName = arguments[0].trim().length() == 0 ? "index" : arguments[0];

        try {
            callAction(actionName);
        } catch (IllegalArgumentException e) {
            log.error("Error executing action " + actionName, e);
            resp.sendError(500, e.getMessage());
            return;
        } catch (SecurityException e) {
            log.error("Error executing action " + actionName, e);
            resp.sendError(500, e.getMessage());
            return;
        } catch (NoSuchMethodException e) {
            log.error("Error executing action " + actionName, e);
            resp.sendError(404);
            return;
        } catch (IllegalAccessException e) {
            log.error("Error executing action " + actionName, e);
            resp.sendError(500, e.getMessage());
            return;
        } catch (InvocationTargetException e) {
            log.error("Error executing action " + actionName, e);
            resp.sendError(500, e.getMessage());
            return;
        } catch (Exception e) {
            log.error("Error executing action " + actionName, e);
            resp.sendError(500, e.getMessage());
            return;
        }

        if (output != null) {
            output.process(actionName, resp);
        }
    }

    private void callAction(String actionName) throws IllegalArgumentException,
            NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
        log.debug("Calling action " + actionName);
        if (!getValidActions().contains(actionName)) {
            throw new NoSuchMethodException();
        }

        Method method = this.getClass().getMethod(actionName + "Action", (Class[]) null);
        try {
            method.invoke(this, (Object[]) null);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw e;
        }
    }

    @SuppressWarnings("rawtypes")
    protected void setOutputData(Map data) {
        this.output.setOutputData(data);
    }

    private Output createOutput() {
        String format = getParameter("format");
        if (format != null && format.equals("json")) {
            return new JSONOutput();
        }

        return new HTMLOutput(getServletContext());
    }

    protected Output getOutput() {
        return this.output;
    }

    protected void setOutput(Output output) {
        this.output = output;
    }

    public abstract List<String> getValidActions();

    @SuppressWarnings("rawtypes")
    protected Map getParameters() {
        return this.request.getParameterMap();
    }

    protected String getParameter(String name) {
        return this.request.getParameter(name);
    }

    protected void setUseLayout(Boolean useLayout) {
        output.setUseLayout(useLayout);
    }
}