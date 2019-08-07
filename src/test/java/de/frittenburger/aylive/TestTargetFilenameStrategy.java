package de.frittenburger.aylive;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.file.TargetFilenameStrategy;

public class TestTargetFilenameStrategy {

	@Rule
	public TemporaryFolder folder= new TemporaryFolder();
	
	
	@Test(expected=IOException.class)
	public void testNotExistingPath() throws IOException {
				
		String filenameTemplate = null;
		File path = new File("not existing file");
		new TargetFilenameStrategy(path, filenameTemplate);
	
	}

	
	@Test
	public void testGetTargetFromContent() throws IOException {
				
		File path = folder.newFolder("targetpath");
		TargetFilenameStrategy  strategy = new TargetFilenameStrategy(path, null);
		
		Content source = new Content("Hello World".getBytes(), "UTF-8");
		source.setName("test.txt");
		File target = strategy.fromContent(source);
		
		assertEquals("test.txt",target.getName());
		
	}
	
	@Test
	public void testGetTargetFromContentWithOutName() throws IOException {
				
		File path = folder.newFolder("targetpath");
		TargetFilenameStrategy  strategy = new TargetFilenameStrategy(path, null);
		
		Content source = new Content("Hello World".getBytes(), "UTF-8");
		File target = strategy.fromContent(source);
		
		assertEquals("b10a8db164e0754105b7a99be72e3fe5.dat",target.getName());
		
	}
	
	@Test
	public void testGetTargetFromContentWithDefaultName() throws IOException {
				
		File path = folder.newFolder("targetpath");
		TargetFilenameStrategy  strategy = new TargetFilenameStrategy(path, "test.txt");
		
		Content source = new Content("Hello World".getBytes(), "UTF-8");
		File target = strategy.fromContent(source);
		
		assertEquals("test.txt",target.getName());
		
	}
	
	@Test
	public void testGetTargetFromContentFileExists() throws IOException {
				
		File path = folder.newFolder("targetpath");
		
		Files.write(new File(path,"test.txt").toPath(), "Hello World".getBytes());
		
		TargetFilenameStrategy  strategy = new TargetFilenameStrategy(path, null);
		
		Content source = new Content("Hello World".getBytes(), "UTF-8");
		source.setName("test.txt");
		File target = strategy.fromContent(source);
		
		assertEquals("test.txt",target.getName());
		
	}
	
	
	@Test
	public void testGetTargetFromContentFileExistsWithOtherContent() throws IOException {
				
		File path = folder.newFolder("targetpath");
		
		Files.write(new File(path,"test.txt").toPath(), "HELLO WORLD".getBytes());
		
		TargetFilenameStrategy  strategy = new TargetFilenameStrategy(path, null);
		
		Content source = new Content("Hello World".getBytes(), "UTF-8");
		source.setName("test.txt");
		File target = strategy.fromContent(source);
		
		assertEquals("test_1.txt",target.getName());
		
	}
	
	
	
	
	@Test
	public void testGetTargetFromContentFileExistsWithOtherName() throws IOException {
				
		File path = folder.newFolder("targetpath");
		
		Files.write(new File(path,"test.txt").toPath(), "HELLO WORLD".getBytes());
		Files.write(new File(path,"test1.txt").toPath(), "Hello World".getBytes());
		
		TargetFilenameStrategy  strategy = new TargetFilenameStrategy(path, null);
		
		Content source = new Content("Hello World".getBytes(), "UTF-8");
		source.setName("test.txt");
		File target = strategy.fromContent(source);
		
		assertEquals("test1.txt",target.getName());
		
	}
	
	@Test
	public void testGetTargetFromFile() throws IOException {
				
		File path = folder.newFolder("targetpath");
		TargetFilenameStrategy  strategy = new TargetFilenameStrategy(path, null);
		
		File source = folder.newFile("test.txt");
		
		File target = strategy.fromFile(source);
		
		assertEquals("test.txt",target.getName());
		
	}
	
	

	
	
}
