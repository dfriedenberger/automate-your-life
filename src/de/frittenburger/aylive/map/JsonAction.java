package de.frittenburger.aylive.map;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Content;

public class JsonAction extends Action {

	private Mapping<JsonNode, JsonNode> mapping;

	@Override
	public Object excecute(Object obj) throws Exception {

		if(obj instanceof Content)
		{
			Content content = (Content)obj;
			if(content.getContentType().equals("application/json"))
			{
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode source = objectMapper.readTree(content.getData());
			
				JsonNode target = mapping.map(source);
				
				Content newcontent = new Content(objectMapper.writeValueAsBytes(target),"UTF-8");
				newcontent.setContentType("application/json");
				newcontent.setName(content.getName());
				
				return newcontent; 
			}					
		}
		
		
		return null;
	
	}

	public JsonAction mapping(Mapping<JsonNode,JsonNode> mapping) {
		this.mapping = mapping;
		return this;
	}

}
