package de.frittenburger.aylive.file;

public class Factory {

	public static FileTrigger NewFile()
	{
		return new FileTrigger();
	}
	
	public static FileAction SaveFile()
	{
		return new FileAction();
	}
	
}
