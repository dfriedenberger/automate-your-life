package de.frittenburger.aylive.mail;

import javax.mail.internet.MimeMessage;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.util.Logger;

public class MailAction extends Action {

	private final Logger logger = new Logger(this.getClass().getSimpleName());
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
			logger.error("No Content matched contentType="+contentType+" nameRegex="+nameRegex);
			return null;
		}
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "extract attachment";
	}


	
	
	

	

}
