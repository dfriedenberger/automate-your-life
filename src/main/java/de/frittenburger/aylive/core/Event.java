package de.frittenburger.aylive.core;

import java.util.List;

public class Event {

	private final List<Recipe> recipes;
	private final Object obj;

	public Event(Object obj, List<Recipe> recipes) {
		
		if(obj == null) 
			throw new IllegalArgumentException();
		if(recipes == null || recipes.size() == 0)
			throw new IllegalArgumentException();

		this.obj = obj;
		this.recipes = recipes;
	}

	
	public List<Recipe> getRecipes() {
		return recipes;
	}

	public Object getObject() {
		return obj;
	}

}
