package de.frittenburger.aylive.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.AddressException;

import com.fasterxml.jackson.databind.JsonNode;

import de.frittenburger.aylive.core.Resource;

public class Factory {
	

	public static MailBox NewMailBox(Iterator<Entry<String, JsonNode>> elements) throws IOException
	{
		MailBox mailbox = new MailBox();

		while(elements.hasNext())
		{
			Entry<String, JsonNode> e = elements.next();
        	String key = e.getKey();
        	JsonNode node = e.getValue();
        	
        	if(key.equals("server"))
        	{
        	   MailProtocol protocol = MailProtocol.valueOf(node.get("protocol").asText().trim().toUpperCase());
        	   String host = node.get("host").asText();
        	   int port = node.get("port").asInt();
        	   boolean ssl = node.get("ssl").asBoolean();
        	   
        	   mailbox.setProvider(protocol,host,port,ssl);
        	   continue;
        	}
        	        	
        	if(key.equals("credentials"))
        	{
        		String user = node.get("user").asText();
        		String pass = node.get("pass").asText();
        		mailbox.setCredentials(user, pass);
         	   continue;

        	}
        	if(key.equals("cycle")) 
        	{
        		mailbox.setCylce(node.asInt());
         	   	continue;
        	}
        	throw new IOException("Unknown key "+key);
		}
		
		return mailbox;
		
	}
	
	
	public static MailTrigger NewMail()
	{
		return new MailTrigger();
	}
	
	public static MailTrigger NewMail(Iterator<Entry<String, JsonNode>> elements,Map<String,Resource> resources) throws IOException, AddressException
	{
		MailTrigger trigger = new MailTrigger();
		while(elements.hasNext())
		{
			Entry<String, JsonNode> e = elements.next();
        	String key = e.getKey();
        	JsonNode node = e.getValue();
        	
        	if(key.equals("in"))
        	{
        		Resource res = resources.get(node.asText());
        		if(!(res instanceof MailBox))
        			throw new IOException("resources has to be a mailbox");
        	    trigger.in((MailBox)res);
        	   continue;
        	}
        	if(key.equals("from"))
        	{
        		trigger.from(getValues(node));
        		continue;
        	}
     
        	throw new IOException("Unknown key "+key);
		}
		return trigger;
	}
	
	private static List<String> getValues(JsonNode node) {

		
		List<String> values = new ArrayList<>();
		if (node.isArray())
	    {
			Iterator<JsonNode> elements = node.elements();
			while(elements.hasNext())
			{
				JsonNode n = elements.next();
				values.add(n.asText());
			}
	    }
		else
		{
			values.add(node.asText());
		}
		return values;
	}


	public static MailAction ExtractAttachment()
	{
		return new MailAction();
	}
	
	public static MailAction ExtractAttachment(Iterator<Entry<String, JsonNode>> elements) throws IOException
	{
		MailAction action = new MailAction();
		while(elements.hasNext())
		{
			Entry<String, JsonNode> e = elements.next();
        	String key = e.getKey();
        	JsonNode node = e.getValue();
        	
        	if(key.equals("withName"))
        	{
        		action.withName(node.asText());
        		continue;
        	}
        	if(key.equals("withContentType"))
        	{
        		action.withContentType(node.asText());
        		continue;
        	}
        	
        	
        	throw new IOException("Unknown key "+key);
		}
		return action;
	}
}
