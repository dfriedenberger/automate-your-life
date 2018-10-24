package de.frittenburger.aylive.core;

import java.util.ArrayList;
import java.util.List;

import de.frittenburger.aylive.util.Logger;

public class Service extends Thread {

	private final List<Recipe> recipes = new ArrayList<Recipe>();
	private final Logger logger = new Logger(this.getClass().getSimpleName());
	private final List<Resource> resources = new ArrayList<Resource>();
	
	
	public Recipe addRecipe(String name) {
		
		Recipe recipe = new Recipe(name);
		recipes.add(recipe);
	
		return recipe;
	}
	
	public void addResource(Resource resource)
	{
		resources.add(resource);
	}
	

	public void run() {
		
		//dump
		for(Recipe recipe : recipes)
			logger.infoFormat("%s", recipe);
		
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
						logger.infoFormat("Excecute %s with %s", recipe,obj);

						for(Action action : recipe.getActionPipe())
						{
							obj = action.excecute(obj);
							if(obj == null) break;
						}	
						
					}
					
				} catch (Exception e) {
						logger.error(e);
						e.printStackTrace();
				}
			
			try {
				sleep(100);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
		
	}



}
