package de.frittenburger.aylive.mail;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.aylive.core.Action;
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

			for(Content content : wrapper.getContents())
			{
				String name = content.getName();
				if(name != null && nameRegex != null)
					if(name.matches(nameRegex))
						return content;
				String type = content.getContentType();
				if(type != null && contentType != null)
				{
					if(type.equals(contentType)) 
						return content;
				}
			}
			logger.error("No Content matched contentType={} nameRegex={}",contentType,nameRegex);
			return null;
		}
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "extract attachment";
	}


	
	
	

	

}
