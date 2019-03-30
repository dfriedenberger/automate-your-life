package de.frittenburger.aylive;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;

import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.mail.MailAction;
import de.frittenburger.aylive.mail.MimeMessageWrapper;

class TestExtractAttachment {

	@Test
	void testMimeMessageWrapper() throws Exception
	{
		InputStream mailFileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("textmail.eml");	
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session, mailFileInputStream);
		
		MimeMessageWrapper wrapper = new MimeMessageWrapper(message);

		List<Content> contents = wrapper.getContents();
		assertEquals(2,contents.size());
		
		assertEquals("Ihre Aufladung: A&B 20 € Automatische Aufladung. Das A&B Guthaben haben wurde auf die folgende Rufnummer aufgeladen:  +4916212345678\r\n",new String(contents.get(0).getData(),"UTF-8"));
		
	}
	
	
	@Test
	void testMailAction() throws Exception {
		
		
		InputStream mailFileInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("textmail.eml");	
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session, mailFileInputStream);
	
		
		MailAction action = new MailAction();
		action.withContentType("text/plain");
		Object obj = action.excecute(message);
		
	
	
		assertTrue(obj instanceof Content);
		
		
		Content content = (Content)obj;
		
		byte[] data = content.getData();
		assertEquals("UTF-8", content.getEncoding()); 
		
		
		String text = new String(data,content.getEncoding());
		
		assertTrue(text.contains("20 €"));

		
		
		
	}

}
