package de.frittenburger.aylive.mail;

import javax.mail.internet.MimeMessage;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Content;

public class MailAction extends Action {

	private String nameRegex = null;

	public Action withName(String pattern) {
		nameRegex = "^" + pattern.replaceAll("[.]", "[.]").replaceAll("[?]", ".").replaceAll("[*]",".*") + "$";
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
				if(name == null) continue;
				if(name.matches(nameRegex))
					return content;
			}
			
			return null;
		}
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "extract attachment";
	}

	
	

	

}
