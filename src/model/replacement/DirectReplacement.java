package model.replacement;

import model.cromosome.Chromosome;

public class DirectReplacement extends Replacement {

	public DirectReplacement() {
		this.replacement = 1;
	}

	@Override
	public Chromosome[] replace(Chromosome[] pob, Chromosome[] new_pob) {
		for (int i = 0; i < new_pob.length; i++)
			new_pob[i].evaluate();
		return new_pob;
	}

	@Override
	public String toString() {
		return "Direct replacement";
	}
}
