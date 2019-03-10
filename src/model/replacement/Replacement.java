package model.replacement;

import model.cromosome.Chromosome;

public abstract class Replacement {
	protected int replacement;
	
    public abstract Chromosome[] replace(Chromosome[] pob, Chromosome[] new_pob);
    
    public int getReplacement() {
    	return replacement;
    }
}
