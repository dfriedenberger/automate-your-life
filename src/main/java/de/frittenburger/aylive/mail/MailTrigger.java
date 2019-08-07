package de.frittenburger.aylive.mail;


import java.util.Arrays;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.frittenburger.aylive.core.Trigger;

public class MailTrigger extends Trigger {

	private List<String> fromAddresses = null;
	
	public MailTrigger from(List<String> list) throws AddressException {
		
		fromAddresses = list;
		for(String adress : fromAddresses)
			new InternetAddress(adress).validate();

		return this;
	}
	
	public MailTrigger from(String adress) throws AddressException {
		return from(Arrays.asList(adress));
	}

	
	
	public MailTrigger in(MailBox mailBox) {
		mailBox.addListener(this);
		return this;
	}
	
	
	@Override
	public String toString() {
		return "new mail from "+fromAddresses;
	}

	@Override
	public boolean match(Object obj) throws Exception {

		if(obj instanceof MimeMessage)
		{
			MimeMessage msg = (MimeMessage)obj;
			
			if (fromAddresses == null) return true; //match every time
				
			Address[] address = msg.getFrom();
			if (address.length != 1)
				throw new ArrayIndexOutOfBoundsException();
			
			InternetAddress emailAddr = (InternetAddress) address[0];
			emailAddr.validate();
			
			for(String fromAddress : fromAddresses)
				if (fromAddress.equals(emailAddr.getAddress())) return true;
			
		}		
		return false;
	}


	

	
}
