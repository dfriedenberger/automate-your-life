package de.frittenburger.aylive.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.AddressException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Recipe;
import de.frittenburger.aylive.core.Resource;
import de.frittenburger.aylive.core.Service;
import de.frittenburger.aylive.core.Trigger;

import static de.frittenburger.aylive.mail.Factory.*;
import static de.frittenburger.aylive.file.Factory.*;

public class ConfigReader {

	private final Map<String,Resource> resources = new HashMap<String,Resource>();
	private final Service service;

	public ConfigReader(Service service)
	{
		this.service = service;
	}
	
	public void init(String filename) {
		 ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	        try {
	            JsonNode node = mapper.readTree(new File(filename));
	            Iterator<Entry<String, JsonNode>> elements = node.fields();
	            while(elements.hasNext())
	            {
	            	Entry<String, JsonNode> e = elements.next();
	            	String key = e.getKey();
	            	
	            	
	            	JsonNode n = e.getValue();
	            	if(n instanceof ObjectNode)
	            	{
		            	ObjectNode o = (ObjectNode)n;
		            	handle(key,o);
			            System.out.println(o);
	            	}
	            	if(n instanceof ArrayNode)
	            	{
	            		ArrayNode a = (ArrayNode)n;
	            		
	            		for(JsonNode o : a)
	            		{
			            	handle(key,(ObjectNode)o);
	            	    }
	            	}

	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }		
	}

	private void handle(String key, ObjectNode o) throws IOException {

		switch(key)
		{
		case "resources":
			handleResource(o);
		    break;
		case "recipes":
			handleRecipe(o);
			break;
		default:
			System.err.println("unknown key "+key);
			break;
		}
	}

	private void handleResource(ObjectNode o) throws IOException {

        Iterator<Entry<String, JsonNode>> elements = o.fields();

        if(!elements.hasNext()) throw new IOException("empty resource");
      
    	Entry<String, JsonNode> e = elements.next();
    	String key = e.getKey();
    	String id = e.getValue().asText();

    	
    	if(key.equals("mail"))
    	{
    		Resource res = NewMailBox(elements);
    		resources.put(id,res);
    		service.addResource(res);
    		return;
    	}
    	
    	if(key.equals("folder"))
    	{
    		Resource res = NewDirectory(elements);
    		resources.put(id,res);
    		service.addResource(res);
    		return;
    	}
      
    	throw new IOException("Unknown Key "+key+" "+id);
		
	}

	private void handleRecipe(ObjectNode o) throws IOException {

		 Iterator<Entry<String, JsonNode>> elements = o.fields();
	     if(!elements.hasNext()) throw new IOException("empty recipe");
	    
	     Entry<String, JsonNode> e = elements.next();
	     String key = e.getKey();
	     if(!key.equals("recipe")) 
	    	 throw new IOException("key 'recipe' has to be definied (instead of "+key+")");
	     Recipe recipe = service.addRecipe(e.getValue().asText());
	     
	     
	     //when
	     if(!elements.hasNext()) throw new IOException("key 'when' has to be definied");
	     e = elements.next();
	     key = e.getKey();
	     if(!key.equals("when")) 
	    	 throw new IOException("'when' has to be definied (instead of "+key+")");
	     recipe.When(handleWhen(e.getValue()));   	
	     
	     //then
	     if(!elements.hasNext()) throw new IOException("key 'then' has to be definied");
	     e = elements.next();
	     key = e.getKey();
	     if(!key.equals("then")) 
	    	 throw new IOException("key 'then' has to be definied (instead of "+key+")");
	     
	     JsonNode n = e.getValue();
		 if(n instanceof ArrayNode)
	     {
	    		ArrayNode a = (ArrayNode)n;
	    		for(JsonNode doNode : a)
	    		{
	    			JsonNode node = doNode.get("do");
	    			if(node == null)
	    				throw new IOException("key 'do' has to be definied");
	    			recipe.Do(handleDo(node));
	    	    }
	    		return;
	     }
	     
		 throw new IOException("unknown structore of yaml");
	     
	}

	private Action handleDo(JsonNode o) throws IOException {
		  Iterator<Entry<String, JsonNode>> elements = o.fields();

	        if(!elements.hasNext()) throw new IOException("empty do");
	      
	    	Entry<String, JsonNode> e = elements.next();
	    	String key = e.getKey();
	    	String action = e.getValue().asText();
	    	if(!key.equals("action")) throw new IOException("key 'action' must be defined (instead of "+key+")");
	    	
	    	if(action.equals("savefile"))
	    	{
	    		return SaveFile(elements);
	    	}
	    	
	    	if(action.equals("extractattachment"))
	    	{
	    		return ExtractAttachment(elements);
	    	}
	    	
	    	throw new IOException("Unknown Action "+action);

	}

	private Trigger handleWhen(JsonNode o) throws IOException {

		
		   Iterator<Entry<String, JsonNode>> elements = o.fields();

	        if(!elements.hasNext()) throw new IOException("empty when");
	      
	    	Entry<String, JsonNode> e = elements.next();
	    	String key = e.getKey();
	    	String event = e.getValue().asText();

	    	
	    	if(!key.equals("event")) throw new IOException("key 'event' must be defined");
	    
	    	if(event.equals("newmail"))
	    	{
	    		try {
					return NewMail(elements,resources);
				} catch (AddressException e1) {
					e1.printStackTrace();
					throw new IOException(e1.getMessage());
				}
	    	}
	     	if(event.equals("newfile"))
	    	{
	    		return NewFile(elements,resources);
	    	}
	      
	    	throw new IOException("Unknown Event "+event);
	}

	

}
