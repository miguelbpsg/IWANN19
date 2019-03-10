package model.selection;

import controller.ChromosomeFactory;
import model.cromosome.Chromosome;

public class Remains implements Selection {

	@Override
	public Chromosome[] select(Chromosome[] pob, int tam_pob) {
		Chromosome[] new_pob = new Chromosome[tam_pob];
		int j = 0;
		for(int i = 0; i < tam_pob; i++) {
			for(int k = 0; k < Math.floor(pob[i].getScore()*tam_pob); j++, k++)
				new_pob[j] = ChromosomeFactory.copyChromosome(pob[i]);
		}
		Selection resto = new Roulette();
		Chromosome[] restos = resto.select(pob, tam_pob - j);
		for(int k = 0; k < tam_pob - j; k++) {
			new_pob[j+k] = restos[k];
		}
		return new_pob;
	}

	@Override
	public String toString() {
		return "Remains";
	}
	
	@Override
	public int getSelection() {
		return 2;
	}
}
