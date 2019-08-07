package de.frittenburger.aylive.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.util.MD5Calculator;

public class TargetFilenameStrategy {

	private File path;
	private String filenameTemplate;

	public TargetFilenameStrategy(File path, String filenameTemplate) throws IOException {
		
		if(!path.isDirectory())
			throw new IOException(path+" must be an existing directory");
		this.path = path;
		this.filenameTemplate = filenameTemplate;
	}

	public File fromFile(File source)  throws IOException {

		String md5 = calulate(source);
		long length = source.length();
		String name = source.getName();
		
		return resolveCandidate(length,md5,name);
		
	}

	public File fromContent(Content source) throws IOException {

		
		String md5 = calulate(source);
		long length = source.getData().length;
		String name = source.getName();
		
		if(name == null)
			name = filenameTemplate;
		
		if(name == null)
			name = "{md5}.dat";
		
		//replace
		if(name.contains("{md5}"))
		{
			name = name.replaceAll("[{]md5[}]", md5);
		}

		return resolveCandidate(length,md5,name);
		
	}
	
	private File resolveCandidate(long length, String md5, String name) throws IOException {
		
		//neues oder existierendes File
		File candidate1 = new File(path,name);
		if(!candidate1.exists()) return candidate1;
		//check if is the same
		if(calulate(candidate1).equals(md5)) return candidate1;
			
	
		for(File candidate2 : path.listFiles())
		{
				if(!candidate2.isFile()) continue;
				if(candidate2.length() != length) continue;
				if(!calulate(candidate2).equals(md5)) continue;
				return candidate2;
		}
			
			
		int ix = name.lastIndexOf(".");
		String ext = ix < 0?"":name.substring(ix);
		String prefix = ix < 0?name:name.substring(0,ix);
		
		
		for(int i = 1;i < 100;i++)
		{
			File candidate3 = new File(path,prefix+ "_" + i + ext);
			
			if(!candidate3.exists()) return candidate3;
			
			if(candidate3.length() == length) 
			{
				//check if is the same
				if(calulate(candidate3).equals(md5)) return candidate3;
			}
			//weiter gehts
		}
			
		
		throw new IOException("could not find name for "+name);
	}

	private String calulate(File file) throws IOException {

		try {
			try (InputStream fis =  new FileInputStream(file)) {
				return MD5Calculator.calculate(fis);
			}
		} 
		catch (GeneralSecurityException e) {
			throw new IOException(e);
		}
	
	}

	private String calulate(Content source) throws IOException {
		try {
			try(InputStream bais = new ByteArrayInputStream(source.getData()))
			{
				return MD5Calculator.calculate(bais);
			}
		} catch (GeneralSecurityException e) {
			throw new IOException(e);
		}
	}

}
