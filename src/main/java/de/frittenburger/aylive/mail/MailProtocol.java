package de.frittenburger.aylive.mail;

public enum MailProtocol {

	POP3("pop3") , 
	IMAP("imap");
	
	private final String protocol;
	
	private MailProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		return protocol;
	}


	
}
