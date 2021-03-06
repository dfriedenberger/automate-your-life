package de.frittenburger.aylive.file;

import java.io.File;
import de.frittenburger.aylive.core.Trigger;

public class FileTrigger extends Trigger {

	private String regex = null;
	private String path = null;

	public FileTrigger in(Directory directory)  {
		directory.addListener(this);
		path = directory.getPath();
		return this;
	}

	public FileTrigger withName(String pattern) {
		regex = "^" + pattern.replaceAll("[.]", "[.]").replaceAll("[?]", ".").replaceAll("[*]",".*") + "$";
		return this;
	}


	@Override
	public boolean match(Object obj) throws Exception {
		
		
		if(obj instanceof File)
		{
			String filename = ((File)obj).getName();
			return filename.matches(regex);
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "new file in "+path;
	}
}
