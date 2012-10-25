package no.spp.example.output;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class JSONOutput implements Output 
{
	@SuppressWarnings("rawtypes")
	private Map data = new HashMap();


	public void process(String action, HttpServletResponse response) throws IOException 
	{
		response.setContentType("application/json");
		response.getWriter().println(JSONObject.fromObject(data));
	}

	public void setUseLayout(Boolean useLayout) 
	{		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setOutputData(Map data) 
	{
		this.data.putAll(data);
	}

}
