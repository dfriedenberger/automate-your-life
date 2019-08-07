package de.frittenburger.aylive.mail;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Collection;
import de.frittenburger.aylive.core.Content;

public class MailAction extends Action {

    private static final Logger logger = LogManager.getLogger(MailAction.class);
	private String nameRegex = null;
	private String contentType = null;

	public MailAction withName(String pattern) {
		nameRegex = "^" + pattern.replaceAll("[.]", "[.]").replaceAll("[?]", ".").replaceAll("[*]",".*") + "$";
		return this;
	}
	
	public MailAction withContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	
	@Override
	public Object excecute(Object obj) throws Exception {
		if(obj instanceof MimeMessage)
		{
			MimeMessage source = (MimeMessage)obj;
			MimeMessageWrapper wrapper = new MimeMessageWrapper(source);

			Collection collection = new Collection();
			
			for(Content content : wrapper.getContents())
			{
				String name = content.getName();
				if(name != null && nameRegex != null)
					if(name.matches(nameRegex))
					{
						collection.add(content);
						continue;
					}
				String type = content.getContentType();
				if(type != null && contentType != null)
				{
					if(type.equals(contentType)) 
					{
						collection.add(content);
						continue;
					}
				}
			}
			
			switch(collection.size())
			{
				case 0:
					logger.error("No Content matched contentType={} nameRegex={} subject={}exit"
						+ "",contentType,nameRegex,source.getSubject());

					return null;
				case 1: //Single Content
					return collection.get(0);
				default:
					return collection;
			}
		}
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "extract attachment";
	}


	
	
	

	

}
