package model.selection;

import model.cromosome.Chromosome;

public interface Selection {
	public Chromosome[] select(Chromosome[] pob, int tam_pob);
	public int getSelection();
}