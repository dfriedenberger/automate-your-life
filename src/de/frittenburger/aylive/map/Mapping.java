package de.frittenburger.aylive.map;

public abstract class Mapping<Ttarget, Tsource> {
	
	public abstract Ttarget map(Tsource source) throws Exception;
	

}
