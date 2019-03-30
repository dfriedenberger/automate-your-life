package de.frittenburger.aylive.app;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.aylive.core.Service;
import de.frittenburger.aylive.util.ConfigReader;

public class AYT {

    private static final Logger logger = LogManager.getLogger(AYT.class);

	public static void main(String[] args) throws InterruptedException {

		// Create a basic service object 
		Service service = new Service();
		
		ConfigReader reader = new ConfigReader(service);
		for(int i = 0;i < args.length;i++)
		{
			if(!new File(args[i]).isFile())
			{
				logger.warn("Unknown argument {}",args[i]);
				continue;
			}
			reader.init(args[i]);
		}
		
		
		
		// Start things up! 
		service.start();
		service.join();
		
		
	}

}
