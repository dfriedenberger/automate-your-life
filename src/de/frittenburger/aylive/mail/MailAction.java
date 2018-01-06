package de.frittenburger.aylive.mail;

import javax.mail.internet.MimeMessage;

import de.frittenburger.aylive.core.Action;

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

			for(Attachment attachment : wrapper.getAttachments())
			{
				String name = attachment.getName();
				if(name == null) continue;
				if(name.matches(nameRegex))
					return attachment;
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
