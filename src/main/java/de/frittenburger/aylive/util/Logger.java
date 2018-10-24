package de.frittenburger.aylive.util;

public class Logger {
	
	private final String name;

	public Logger(String name)
	{
		this.name = name;
	}

	public void infoFormat(String format, Object... args) {
		System.out.println("[INFO] "+name+" "+String.format(format, args));		
	}
	
	public void info(Object obj) {
		System.out.println("[INFO] "+name+" "+obj);		
	}
	
	public void errorFormat(String format, Object... args) {
		System.err.println("[ERROR] "+name+" "+String.format(format, args));				
	}
	
	public void error(Object obj) {
		System.err.println("[ERROR] "+name+" "+obj);				
	}

}
