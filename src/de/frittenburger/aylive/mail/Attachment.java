package de.frittenburger.aylive.mail;


import javax.mail.internet.ContentType;

public class Attachment {

	

	private ContentType contentType;
	private byte[] data;

	public Attachment(ContentType contentType, byte[] data) {
		this.contentType = contentType;
		this.data = data;
	}
	
	public String getName() {
		return contentType.getParameter("name");
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Attachment [contentType=" + contentType + ", len=" + data.length + "]";
	}

	
	

}
