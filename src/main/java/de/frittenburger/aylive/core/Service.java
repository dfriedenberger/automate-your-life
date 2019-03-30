package de.frittenburger.aylive.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Service extends Thread {

	private final List<Recipe> recipes = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Service.class);
	private final List<Resource> resources = new ArrayList<>();
	
	
	public Recipe addRecipe(String name) {
		
		Recipe recipe = new Recipe(name);
		recipes.add(recipe);
	
		return recipe;
	}
	
	public void addResource(Resource resource)
	{
		resources.add(resource);
	}
	
	@Override
	public void run() {
		
		//dump
		for(Recipe recipe : recipes)
			logger.info("{}", recipe);
		
		while(true)
		{
			
			//poll resources
			for(Resource resource : resources)
				try {
					Event event = resource.poll();
					if(event == null) continue;
					
					for(Recipe recipe :  event.getRecipes())
					{
						Object obj = event.getObject();
						logger.info("Excecute {} with {}", recipe,obj);

						for(Action action : recipe.getActionPipe())
						{
							obj = action.excecute(obj);
							if(obj == null) break;
						}	
						
					}
					
				} catch (Exception e) {
					logger.error(e);
				}
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				logger.error(e);
				// Restore interrupted state...
			    Thread.currentThread().interrupt();
			}
		}
		
	}



}
