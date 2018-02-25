package de.frittenburger.aylive.mail;

import java.util.Enumeration;

import javax.mail.Header;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;

public class HeaderWrapper {

	private ContentType contentType = null;
	private ContentDisposition contentDisposition = null;

	public HeaderWrapper(Enumeration<Header> headers) throws ParseException {

		
		while(headers.hasMoreElements())
		{
			Header h = headers.nextElement();
			String name = h.getName().toLowerCase();
			
			//Content-Type: multipart/mixed; boundary="multipart_mixed.303870418"
			if(name.equals("content-type"))
				contentType = new ContentType(h.getValue());
			if(name.equals("content-disposition"))
				contentDisposition = new ContentDisposition(h.getValue());
		}
		
	}

	String getContentType() {
		if(contentType != null)
			return contentType.getBaseType();
		return null;
	}

	public String getName() {
		if(contentType != null)
		{
				String name = contentType.getParameter("name");	
				if(name != null) 
					return name;
		}
		
		if(contentDisposition != null)
		{
				String name = contentDisposition.getParameter("filename");	
				if(name != null) 
					return name;
		}
		
		return null;
	}

	public String getEncoding() {
		return contentType.getParameter("encoding");
	}
	
	@Override
	public String toString() {
		return "HeaderWrapper [contentType=" + contentType + ", contentDisposition=" + contentDisposition + "]";
	}

	
	
	

}
