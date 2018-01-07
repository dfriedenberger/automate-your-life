package de.frittenburger.aylive.file;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Content;

public class FileAction extends Action {

	private String path = null;

	public FileAction to(String path) {
		if(!new File(path).isDirectory())
			throw new IllegalArgumentException();
		this.path = path;
		return this;
	}
	
	
	@Override
	public String toString() {
		return "save file to "+path;
	}

	@Override
	public Object excecute(Object obj) throws Exception {

		if(obj instanceof File)
		{
			File source = (File)obj;
			File target = new File(path,source.getName());
			Files.copy(source.toPath(), target.toPath());
			
			return target;
		}
		
		if(obj instanceof Content)
		{
			Content source = (Content)obj;
			File target = new File(path,source.getName());
			
			FileOutputStream fos = new FileOutputStream(target);
			fos.write(source.getData());
			fos.close();
				
			
			return target;
		}
		
	
		
		
		throw new UnsupportedOperationException();
	}

	
	

}
