package de.frittenburger.aylive.file;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;

import de.frittenburger.aylive.core.Resource;


public class Factory {

	public static Directory NewDirectory(Iterator<Entry<String, JsonNode>> elements) throws IOException
	{
		Directory directory = null;

		while(elements.hasNext())
		{
			Entry<String, JsonNode> e = elements.next();
        	String key = e.getKey();
        	JsonNode node = e.getValue();
        	
        	if(key.equals("path"))
        	{
        		directory = new Directory(node.asText());
        		continue;
        	}
        	if(directory == null)  throw new IOException("path have to be defined");
        	throw new IOException("Unknown key "+key);

		}
		return directory;
	}

	public static FileTrigger NewFile()
	{
		return new FileTrigger();
	}
	
	public static FileTrigger NewFile(Iterator<Entry<String, JsonNode>> elements,Map<String,Resource> resources) throws IOException
	{
		FileTrigger trigger = new FileTrigger();
		while(elements.hasNext())
		{
			Entry<String, JsonNode> e = elements.next();
        	String key = e.getKey();
        	JsonNode node = e.getValue();
        	
        	if(key.equals("in"))
        	{
        		Resource res = resources.get(node.asText());
        		if(!(res instanceof Directory))
        			throw new IOException("resources has to be a mailbox");
        	    trigger.in((Directory)res);
        	   continue;
        	}
        	
        	if(key.equals("withName"))
        	{
        		trigger.withName(node.asText());
        		continue;
        	}
     
        	throw new IOException("Unknown key "+key);
		}
		return trigger;
	}
	
	public static FileAction SaveFile()
	{
		return new FileAction();
	}
	
	public static FileAction SaveFile(Iterator<Entry<String, JsonNode>> elements) throws IOException
	{
		FileAction action = new FileAction();
		while(elements.hasNext())
		{
			Entry<String, JsonNode> e = elements.next();
        	String key = e.getKey();
        	JsonNode node = e.getValue();
        	
        	if(key.equals("target"))
        	{
        		action.to(node.asText());
        		continue;
        	}
        	if(key.equals("useName"))
        	{
        		action.useName(node.asText());
        		continue;
        	}
        	throw new IOException("Unknown key "+key);
		}
		return action;
	}
	
	
}
