package de.frittenburger.aylive.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.util.Logger;

public class MimeMessageWrapper {
	
	private final Logger logger = new Logger(this.getClass().getSimpleName());
	private final List<Content> contents = new ArrayList<Content>();
	
	@SuppressWarnings("unchecked")
	public MimeMessageWrapper(MimeMessage message) throws  MessagingException, IOException {

		//parse
		parseContent(message.getContent(),new HeaderWrapper(message.getAllHeaders()));
			
	}
	
	public List<Content> getContents() {
		return contents;
	}
	
	@SuppressWarnings("unchecked")
	private void parseContent(Object msgContent,HeaderWrapper headerWrapper) throws MessagingException, IOException {

		
		logger.info(headerWrapper);
		
		if (msgContent instanceof Multipart) {
			Multipart multipart = (Multipart) msgContent;
						
			for (int j = 0; j < multipart.getCount(); j++) {

				BodyPart bodyPart = multipart.getBodyPart(j);
				parseContent(bodyPart.getContent(),new HeaderWrapper(bodyPart.getAllHeaders()));
			}
			

		} else {
			
			if (msgContent instanceof String) {
				String str = (String) msgContent;	
				
				Content content = new Content(str.getBytes("UTF-8"),"UTF-8");
				content.setContentType(headerWrapper.getContentType());
				content.setName(headerWrapper.getName());
				contents.add(content);
				
			} else if (msgContent instanceof InputStream) {
				// BASE64DecoderStream
				// QPDecoderStream
				// SharedByteArrayInputStream
				InputStream is = (InputStream) msgContent;
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[16384];
				while ((nRead = is.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
				}
				buffer.flush();
				
				Content content = new Content(buffer.toByteArray(),headerWrapper.getEncoding());
				content.setContentType(headerWrapper.getContentType());
				content.setName(headerWrapper.getName());
				contents.add(content);
				
			} else {
				throw new RuntimeException("Not implemented " + msgContent);
			}
		}

	}

	

}
