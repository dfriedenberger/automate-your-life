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

	private MailProtocol protocol = null;
	private String mailserver = null;
	private int port = 995;
	private boolean ssl = true;
	private String username = null;
	private String password = null;
	
	
	private static Cache cache = Cache.getInstance("mail");
	private final Logger logger = new Logger(this.getClass().getSimpleName());
	private long cycleMin = 1;
	private long last = 0;
	private int cntMessages = 0;

	

	
	public void setProvider(MailProtocol protocol, String mailserver, int port, boolean ssl) {
		this.protocol = protocol;
		this.mailserver = mailserver;
		this.port = port;
		this.ssl = ssl;
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
		final Session session;
		if(ssl)
		{
			String prefix = "mail."+protocol;
			props.setProperty(prefix+".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty(prefix+".socketFactory.fallback", "false");
			session = Session.getInstance(props);
		}
		else
		{
			session = Session.getDefaultInstance(props, null);
		}
		store = session.getStore(protocol.toString());
		store.connect(mailserver, port, username, password);
		inbox = store.getFolder(folder);

		inbox.open(Folder.READ_ONLY);
		
	}
	
	private void close() {

		if (inbox != null)
			try {
				if(inbox.isOpen())
					inbox.close(false);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		inbox = null;

		if (store != null)
			try {
				store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		store = null;

	}

	@Override
	public Event poll() throws Exception 
	{
		
		if((new Date().getTime() - last) < (cycleMin * 60 * 1000)) return null;
		
		
		try
		{
		
			open("INBOX");
			
			Message[] messages = inbox.getMessages();
			Event event = getUnread(messages,0,messages.length-1);
			if(event != null) 
				return event;
			
			last  = new Date().getTime();
			if(cntMessages > 0)
				logger.info("read "+cntMessages+" messages");
			cntMessages = 0;
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
			cntMessages++;
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
