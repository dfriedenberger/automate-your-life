package de.frittenburger.aylive.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.core.Event;
import de.frittenburger.aylive.core.Recipe;
import de.frittenburger.aylive.core.Resource;
import de.frittenburger.aylive.util.Logger;

public class WebApi extends Resource {

	private String url;
	private long cycleMin = 1;
	private long last = 0;
	private final Logger logger = new Logger(this.getClass().getSimpleName());

	public WebApi(String url) {
		this.url = url;
	}

	public void set(String key, String value) throws UnsupportedEncodingException {
		url += ((url.indexOf("?") > 0) ? "&" : "?") + URLEncoder.encode(key, "UTF-8") + "="+URLEncoder.encode(value, "UTF-8");
	}
	
	public void setCylce(int cycleMin) {
		this.cycleMin = cycleMin;
	}

	
	@Override
	public Event poll() throws Exception {
		if((new Date().getTime() - last) < (cycleMin * 60 * 1000)) return null;
		
		last = new Date().getTime();
		
		URL obj = new URL(url);
		logger.infoFormat("Sending 'GET' request to URL %s",url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		logger.infoFormat("Response Code : %d",responseCode);	
		
		String contentType = con.getHeaderFields().get("Content-Type").get(0);
		logger.infoFormat("contentType = %s",contentType);

		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		
		Content content = new Content(response.toString().getBytes("UTF-8"),"UTF-8");
		content.setContentType(contentType.split(";")[0].trim());
		content.setName("response.json");
		
		
		List<Recipe> recipes = match(content);

		if (recipes != null)
			return new Event(content,recipes);
		
		
		return null;
	}

	
	

}
