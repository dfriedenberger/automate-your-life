package de.frittenburger.aylive.mail;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import de.frittenburger.aylive.core.Event;
import de.frittenburger.aylive.core.Recipe;
import de.frittenburger.aylive.core.Resource;
import de.frittenburger.aylive.util.Cache;
import de.frittenburger.aylive.util.Logger;

public class MailBox extends Resource {

	private Store store = null;
	private Folder inbox = null;

	private final String provider;
	private final String mailserver;
	private String username = null;
	private String password = null;
	
	
	private static Cache cache = Cache.getInstance("mail");
	private final Logger logger = new Logger(this.getClass().getSimpleName());
	private long cycleMin = 1;
	private long last = 0;
	
	public MailBox(String provider,String mailserver) {
		this.provider = provider;
		this.mailserver = mailserver;
	}

	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public void setCylce(int cycleMin) {
		this.cycleMin = cycleMin;
	}

	private void open(String folder) throws MessagingException {
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		store = session.getStore(provider);
		store.connect(mailserver, username, password);
		
		inbox = store.getFolder(folder);

		inbox.open(Folder.READ_ONLY);
		
	}
	
	private void close() {
			
			try {
				if(inbox != null)
					inbox.close(false);
				if(store != null)
					store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			} finally {
				store = null;
			}	
			
		}


	@Override
	public Event poll() throws Exception 
	{
		
		if((new Date().getTime() - last) < (cycleMin * 60 * 1000)) return null;
		
		
		logger.info("read messages");
		try
		{
		
			open("INBOX");
			
			Message[] messages = inbox.getMessages();
			Event event = getUnread(messages,0,messages.length-1);
			if(event != null) 
				return event;
			
			last  = new Date().getTime();
		}
		finally {
			close();
		}
		return null;
		
	}
	
	private Event getUnread(Message[] messages, int s, int e) throws Exception {
		
		if(e < s)
		{
			throw new IllegalArgumentException();
		}
		
		MimeMessage mimeS = (MimeMessage) messages[s];
		MimeMessage mimeE = (MimeMessage) messages[e];

		String ids = mimeS.getMessageID();
		String ide = mimeE.getMessageID();
		
		if(cache.exists(ids) && cache.exists(ide)) return null;
		
		if(s == e)
		{
			logger.infoFormat("Read Message %d",s);
			//add to cache
			cache.add(ids);
			
			List<Recipe> recipes = match(mimeS);

			if (recipes != null)
				return new Event(new MimeMessage(mimeS),recipes);
			return null;
		}
		
		
		int m = (s + e) / 2;

		Event ev = getUnread(messages,s,m);
		if(ev == null)
			ev = getUnread(messages,m+1,e);
		
		return ev;
	}

	
	
	
	
	
}
