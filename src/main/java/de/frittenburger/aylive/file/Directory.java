package de.frittenburger.aylive.file;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;

import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.core.Event;
import de.frittenburger.aylive.core.Recipe;
import de.frittenburger.aylive.core.Resource;
import de.frittenburger.aylive.util.Cache;
import de.frittenburger.aylive.util.Logger;

public class Directory extends Resource {

	private WatchService watchService = null;
	private String path = null;
	private static Cache cache = Cache.getInstance("file");
	private final Logger logger = new Logger(this.getClass().getSimpleName());
	private final List<FileState> fileQueue = new ArrayList<FileState>();
	
	public Directory(String path) throws IOException {

		if(!new File(path).isDirectory())
			throw new IOException(path+ " is not a directory");
		this.path = path;
		
	}
	public String getPath() {
		return this.path;
	}
	private void scanFolder() {
		
		logger.infoFormat("Scan Folder %s", path);
		for (String filename : new File(path).list()) {
			File file = new File(path, filename);
			if (cache.exists(file.getAbsolutePath()))
				continue;		
			fileQueue.add(new FileState(file));
		}
		
	}
	
	@Override
	public Event poll() throws Exception {
		
		if (watchService == null) {
			watchService = FileSystems.getDefault().newWatchService();
			Path directory = Paths.get(path);
			directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
			scanFolder();
		} else {
			WatchKey wk = watchService.poll();
			if (wk != null)
			{
				scanFolder();

				for (@SuppressWarnings("unused")
				WatchEvent<?> event : wk.pollEvents()) {
				}
				wk.reset();
			}
		}


		for(int i = fileQueue.size() - 1; i >= 0; i--)
		{
			FileState filestate = fileQueue.get(i);
			
			//is current writing ?
			if(filestate.isInProcessing()) continue;
			
			File file = filestate.getFile();
			
			//remove from queue and add to cache
			fileQueue.remove(i);
			cache.add(file.getAbsolutePath());
			
			List<Recipe> recipes = match(file);

			if (recipes != null)
			{
				byte b[] = Files.readAllBytes(file.toPath());
				String ct = Files.probeContentType(file.toPath());
				
				Content content = new Content(b,"UTF-8");
				content.setContentType(ct);
				content.setName(file.getName());
				return new Event(content,recipes);
				//return new Event(file,recipes);
			}
		}

		return null;
	}

	

	

	

}
