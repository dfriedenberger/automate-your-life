package de.frittenburger.aylive.file;

public class Factory {

	public static FileTrigger NewFile()
	{
		return new FileTrigger();
	}
	
	public static FileAction CopyFile()
	{
		return new FileAction();
	}
	
	public static FileAction MoveFile()
	{
		return new FileAction().deleteOnFinish();
	}
}
