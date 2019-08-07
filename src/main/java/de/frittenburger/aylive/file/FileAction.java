package de.frittenburger.aylive.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.aylive.core.Action;
import de.frittenburger.aylive.core.Content;

public class FileAction extends Action {

    private static final Logger logger = LogManager.getLogger(FileAction.class);

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
			
			TargetFilenameStrategy targetFilenameStrategy = new TargetFilenameStrategy(new File(path),filename);
			File target = targetFilenameStrategy.fromFile(source);

			if(target.exists())
			{
				logger.info("target exists {}",target.toPath());
				return target;
			}
			//File target = new File(path,source.getName());
			Files.copy(source.toPath(), target.toPath());
			
			logger.info("copy {} to {}",source.toPath(), target.toPath());
			return target;
		}
		
		if(obj instanceof Content)
		{
			Content source = (Content)obj;
			String name = filename;
			if(name == null)
				name = source.getName();
			
			
			/*
			
				if(name == null)
					name = "{guid}.dat";
				
				name = name.replace("{guid}", UUID.randomUUID().toString());
			*/
			TargetFilenameStrategy targetFilenameStrategy = new TargetFilenameStrategy(new File(path),filename);
			
			File target = targetFilenameStrategy.fromContent(source);
			
			if(target.exists())
			{
				//an has same md5 sum
				logger.info("target exists {}",target.toPath());
				return target;
			}
			
			try(FileOutputStream fos = new FileOutputStream(target))
			{
				fos.write(source.getData());
			}
			logger.info("copy content to {}",target.toPath());
			return target;
		}
		
	
		
		
		throw new UnsupportedOperationException();
	}


	public FileAction useName(String filename) {
		 this.filename = filename;
		 return this;
	}

	
	

}
