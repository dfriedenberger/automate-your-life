package de.frittenburger.aylive.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Content;

public class FileAction extends Action {

	private String path = null;
	private String filename = null;

	public FileAction to(String path) throws IOException {
		if(!new File(path).isDirectory())
			throw new IOException(path +" is not a directory");
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
			
			String name = filename;
			
			if(name == null)
				name = source.getName();
			
			if(name == null)
				name = "{guid}.dat";
			
			name = name.replace("{guid}", UUID.randomUUID().toString());

			File target = new File(path,name);
			
			try(FileOutputStream fos = new FileOutputStream(target))
			{
				fos.write(source.getData());
			}
				
			
			return target;
		}
		
	
		
		
		throw new UnsupportedOperationException();
	}


	public FileAction useName(String filename) {
		 this.filename = filename;
		 return this;
	}

	
	

}
