package de.frittenburger.aylive.map;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Content;

public class MapAction extends Action {

	private Mapping<Object, Object> mapping;

	@Override
	public Object excecute(Object obj) throws Exception {

		if(obj instanceof Content)
		{
			ObjectMapper objectMapper = new ObjectMapper();

			Content content = (Content)obj;
			if(content.getContentType().equals("application/json"))
			{
				JsonNode source = objectMapper.readTree(new String(content.getData(),content.getEncoding()));
			
				JsonNode target = (JsonNode) mapping.map(source);
				
				Content newcontent = new Content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(target),"UTF-8");
				newcontent.setContentType("application/json");
				newcontent.setName(content.getName());
				
				return newcontent; 
			}	
			if(content.getContentType().equals("text/html"))
			{
				Document source = Jsoup.parse(new String(content.getData(),content.getEncoding()));
			
				JsonNode target = (JsonNode) mapping.map(source);
				
				Content newcontent = new Content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(target),"UTF-8");
				newcontent.setContentType("application/json");
				newcontent.setName(content.getName());
				
				return newcontent; 
			}			
		}
		
		
		return null;
	
	}

	
	@SuppressWarnings("unchecked")
	public Action mapping(Mapping<? extends Object, ? extends Object> mapping) {
		this.mapping =  (Mapping<Object, Object>) mapping;
		return this;
	}

	

}
