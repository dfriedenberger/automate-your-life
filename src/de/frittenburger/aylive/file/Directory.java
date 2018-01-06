package de.frittenburger.aylive.file;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

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
	
	public Directory(String path) throws IOException {

		if(!new File(path).isDirectory())
			throw new IllegalArgumentException();
		this.path = path;
		
	}

	@Override
	public Event poll() throws Exception {
		
		if (watchService == null) {
			watchService = FileSystems.getDefault().newWatchService();
			Path directory = Paths.get(path);
			directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		} else {
			WatchKey wk = watchService.poll();
			if (wk == null)
				return null;

			for (@SuppressWarnings("unused")
			WatchEvent<?> event : wk.pollEvents()) {
			}
			wk.reset();
		}

		logger.infoFormat("Scan Folder %s", path);

		for (String filename : new File(path).list()) {
			File file = new File(path, filename);

			if (cache.exists(file.getAbsolutePath()))
				continue;
			cache.add(file.getAbsolutePath());

			List<Recipe> recipes = match(file);

			if (recipes != null)
				return new Event(file,recipes);
		}

		return null;
	}

	

}
