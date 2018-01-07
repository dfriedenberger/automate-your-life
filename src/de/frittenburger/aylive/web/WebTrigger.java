package de.frittenburger.aylive.web;

import de.frittenburger.aylive.core.Content;
import de.frittenburger.aylive.core.Trigger;

public class WebTrigger extends Trigger {

	@Override
	public boolean match(Object obj) throws Exception {

		
		if(obj instanceof Content)
		{
			return true;
		}
		
		return false;
	}

	public Trigger in(WebApi webApi) {
		webApi.addListener(this);
		return this;		
	}
	
	@Override
	public String toString() {
		return "new web response";
	}

}
