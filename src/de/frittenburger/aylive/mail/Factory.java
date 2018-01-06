package de.frittenburger.aylive.mail;

public class Factory {
	
	public static MailTrigger NewMail()
	{
		return new MailTrigger();
	}
	
	public static MailAction ExtractAttachment()
	{
		return new MailAction();
	}
	
}
