package no.spp.example.output;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface Output 
{
	public void process(String action, HttpServletResponse response) throws IOException;
	
	public void setUseLayout(Boolean useLayout);

	@SuppressWarnings("rawtypes")
	public void setOutputData(Map data);
}
