package de.frittenburger.aylive.core;

public abstract class Trigger {

	private Recipe recipe = null;

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Recipe getRecipe() {
		return recipe ;
	}
	
	
	
	public abstract boolean match(Object obj) throws Exception;

	


}
