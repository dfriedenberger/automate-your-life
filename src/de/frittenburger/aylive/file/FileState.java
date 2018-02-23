package de.frittenburger.aylive.file;

import java.io.File;
import java.util.Date;

public class FileState {

	private File file;
	private long lastmodified = -1;
	private long saveLastModified = -1;
	private long saveLength = -1;

	public FileState(File file) {
		this.file = file;
	}

	private void setLastModify() {

		long lm = file.lastModified();
		long ln = file.length();
		
		if(lm == saveLastModified && ln == saveLength) return ; //no changes
		saveLastModified = lm;
		saveLength = ln;
		lastmodified = new Date().getTime();
		//System.out.println(file.getName() + " length="+ln+" modoified="+lm);
	}
	
	public boolean isInProcessing() {
		setLastModify();
		return lastmodified + 2000 > new Date().getTime();
	}

	public File getFile() {
		return file;
	}


}
