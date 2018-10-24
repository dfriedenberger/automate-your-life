package de.frittenburger.aylive.core;


public class Content {

	private String contentType;
	private String name;
	private String encoding;
	private byte[] data;

	public Content(byte[] data,String encoding) {
		this.data = data;
		this.encoding = encoding;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEncoding() {
		return encoding;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Content [contentType=" + contentType + ", name=" + name + ", len=" + data.length+"]";
	}
	
}
