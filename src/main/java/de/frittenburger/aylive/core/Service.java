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
			logger.info("Recipe {}", recipe);
		
		for(Resource resource : resources)
			logger.info("Resource {}", resource);

		
		while(true)
		{
			
			//poll resources
			for(Resource resource : resources)
				try {
					logger.trace("Poll {}", resource);

					Event event = resource.poll();
					if(event == null) continue;
					

					for(Recipe recipe :  event.getRecipes())
					{
						Object obj = event.getObject();
						logger.info("Excecute {} with {}", recipe,obj);

						for(Action action : recipe.getActionPipe())
						{
							
							if(obj instanceof Collection)
							{
								Collection results = new Collection();
								for(Object cobj : (Collection)obj)
								{
									cobj = action.excecute(cobj);
									if(cobj == null) continue;
									results.add(cobj);
								}
								if(results.size() == 0) break;
								obj = results.size() == 1?results.get(0):results;
							}
							else
							{
								obj = action.excecute(obj);
								if(obj == null) break;
							}
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
