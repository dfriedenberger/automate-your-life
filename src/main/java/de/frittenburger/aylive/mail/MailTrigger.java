package de.frittenburger.aylive.mail;


import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.frittenburger.aylive.core.Trigger;

public class MailTrigger extends Trigger {

	private String fromAddress = null;
	
	public MailTrigger from(String address) throws AddressException {
		new InternetAddress(address).validate();
		fromAddress = address;
		return this;
	}
	
	public MailTrigger in(MailBox mailBox) {
		mailBox.addListener(this);
		return this;
	}
	
	
	@Override
	public String toString() {
		return "new mail from "+fromAddress;
	}

	@Override
	public boolean match(Object obj) throws Exception {

		if(obj instanceof MimeMessage)
		{
			MimeMessage msg = (MimeMessage)obj;
			if (fromAddress != null) {
				
				Address[] address = msg.getFrom();
				if (address.length != 1)
					throw new ArrayIndexOutOfBoundsException();
				
				InternetAddress emailAddr = (InternetAddress) address[0];
				emailAddr.validate();
				if (!fromAddress.equals(emailAddr.getAddress())) return false;
			}
			return true;
			
		}		
		
		return false;
	}

	

	
}
