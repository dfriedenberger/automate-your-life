package de.frittenburger.aylive.core;

import java.util.ArrayList;
import java.util.List;


public class Recipe {

	private final String description;
	private Trigger trigger = null;
	private final List<Action> actionPipe = new ArrayList<Action>(); 

	public Recipe(String description) {
		this.description = description;
	}


	
	public Recipe When(Trigger trigger) {
		trigger.setRecipe(this);
		this.trigger = trigger;
		return this;
	}

	public Recipe Do(Action action) {
		actionPipe.add(action);
		return this;
	}

	public List<Action> getActionPipe() {
		return actionPipe;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		
		String str = description + ": When "+trigger;
		
		for(int i = 0;i < actionPipe.size();i++)
			str += (i == 0?" do ":" and ")+actionPipe.get(i);
		
		return str;
		
	}

	

	
	
}
