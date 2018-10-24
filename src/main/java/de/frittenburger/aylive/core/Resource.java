package de.frittenburger.aylive.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Resource {

	
	private final List<Trigger> listener = new ArrayList<Trigger>();
	
	public void addListener(Trigger trigger) {
		listener.add(trigger);
	}
	
	

	public List<Recipe> match(Object obj) throws Exception {
		
		List<Recipe> recipes = new ArrayList<Recipe>();
		for(Trigger trigger : listener)
		{
			if(trigger.match(obj))
				recipes.add(trigger.getRecipe());
		}
			
		if(recipes.size() == 0)
			return null;
		return recipes;
		
	}
	
	public abstract Event poll() throws Exception;
	

}
